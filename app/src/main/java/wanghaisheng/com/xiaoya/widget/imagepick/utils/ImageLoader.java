package wanghaisheng.com.xiaoya.widget.imagepick.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.util.LruCache;
import android.util.DisplayMetrics;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

/**
 * Created by sheng on 2016/1/1.
 */
public class ImageLoader {
    private static ImageLoader mInstance;

    //图片缓存的核心对象
    private LruCache<String,Bitmap> mLruCache;

    //默认的线程池
    private ExecutorService mThreadPool;
    //默认的线程数量
    private static final int DEFAULT_THREAD_COUNT = 1;
    //线程的调度方式：默认为后进先出
    private DispType mDispType = DispType.LIFO;

    //任务队列
    private LinkedList<Runnable> mTaskQueue;

    //后台轮询线程
    private Thread mPoolThread;
    private Handler mPoolThreadHandler;
    private Semaphore mSemaphorePoolThreadHandler = new Semaphore(0);

    private Semaphore mSemaphorePoolThread;


    private Handler mUIHandler;

    private ImageLoader(int threadCount,DispType dispType) {
        init(threadCount,dispType);
    }

    /**
     * 初始化
     * @param threadCount
     * @param dispType
     */
    private void init(int threadCount, DispType dispType) {
        //后台轮询线程
        mPoolThread = new Thread(new Runnable() {
            @Override
            public void run() {
                Looper.prepare();

                mPoolThreadHandler = new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        //线程池取出一个任务去执行
                        mThreadPool.execute(getTask());

                        try {
                            mSemaphorePoolThread.acquire();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                };

                mSemaphorePoolThreadHandler.release();

                Looper.loop();
            }
        });

        mPoolThread.start();

        //获取应用最大的可用内存
        int maxMemory = (int) Runtime.getRuntime().maxMemory();
        //获取lruCache内存
        int lruCacheMemory = maxMemory/8;

        mLruCache = new LruCache<String, Bitmap>(lruCacheMemory) {
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getByteCount()*value.getHeight();
            }
        };

        //创建线程池
        mThreadPool = Executors.newFixedThreadPool(threadCount);
        mTaskQueue = new LinkedList<>();
        mDispType = dispType;

        mSemaphorePoolThread = new Semaphore(threadCount);
    }

    /**
     * 根据path为ImageView设置图片
     * @param path
     * @param imageView
     */
    public void loadImage(final String path, final ImageView imageView) {
        imageView.setTag(path);
        if(mUIHandler == null) {
            mUIHandler = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    //获取得到图片，为ImageView回调设置图片
                    ImageBeanHolder holder = (ImageBeanHolder) msg.obj;
                    String mPath = holder.path;
                    ImageView imageView = holder.imageView;
                    Bitmap mBitmap = holder.bitmap;

                    //将imageView的tag与holder的path进行比较
                    if(imageView.getTag().toString().equals(mPath)) {
                        imageView.setImageBitmap(mBitmap);
                    }
                }
            };
        }

        Bitmap bitmap = getBitmapFromLruCache(path);
        if(null != bitmap) {
            refreshBitmap(bitmap,path,imageView);
        } else {
            addTask(new Runnable(){

                @Override
                public void run() {
                    //加载图片
                    //压缩图片
                    //1、获取图片需要显示的大小
                    ImageViewSize imageViewSize = getImageViewSize(imageView);
                    //根据图片需要显示的宽和高进行压缩
                    Bitmap bitmap = decodeSampledBitmapFromPath(path,imageViewSize.width,imageViewSize.height);
                    //将bitmap写入LruBitmapCache
                    addBitmapToLruCache(path,bitmap);

                    refreshBitmap(bitmap,path,imageView);

                    mSemaphorePoolThread.release();
                }
            });
        }
    }

    private void refreshBitmap(Bitmap bitmap,String path,ImageView imageView) {
        ImageBeanHolder holder = new ImageBeanHolder();
        holder.bitmap = bitmap;
        holder.path = path;
        holder.imageView = imageView;

        Message message = Message.obtain();
        message.obj = holder;
        mUIHandler.sendMessage(message);
    }

    /**
     * 将bitmap写入LruBitmapCache
     * @param path
     * @param bitmap
     */
    private void addBitmapToLruCache(String path, Bitmap bitmap) {
        if(getBitmapFromLruCache(path) == null) {
            if(bitmap != null) {
                mLruCache.put(path,bitmap);
            }
        }
    }

    /**
     * 根据图片需要显示的宽和高进行压缩
     * @param path
     *
     * @return
     */
    private Bitmap decodeSampledBitmapFromPath(String path,int width,int height) {
        //获取图片的宽和高，并不把图片加载到内存中
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path,options);

        //使用获取到的inSampleSize再次解析图片
        options.inSampleSize = calculateInSampleSize(options,width,height);

        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(path,options);

    }


    /**
     * 计算图片的压缩比
     * @param options
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    private int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        //获取图片实际的width和height
        int width = options.outWidth;
        int height = options.outHeight;
        int inSampleSize = 1;

        if(width>reqHeight || height>reqHeight) {
            int sampleWidth = Math.round(width*1.0f/reqWidth);
            int sampleHeight = Math.round(height*1.0f/reqHeight);

            inSampleSize = Math.max(sampleWidth,sampleHeight);
        }

        return inSampleSize;
    }

    /**
     * 根据ImageView获取适当的压缩宽和高
     * @param imageView
     * @return
     */
    private ImageViewSize getImageViewSize(ImageView imageView) {
        ImageViewSize imageViewSize = new ImageViewSize();

        ViewGroup.LayoutParams lp = imageView.getLayoutParams();
        DisplayMetrics displayMetrics = imageView.getContext().getResources().getDisplayMetrics();
        int width = imageView.getWidth();//获取imageView的实际宽度
        if(width <= 0) {
            width = lp.width;//获取imageView在layout中声明的宽度：有可能为wrap_content=-1,match_parent=-2
        }
        if(width <=0) {
            width = imageView.getMaxWidth();//检查最大值
        }
        if(width <= 0) {
            width = displayMetrics.widthPixels;
        }

        int height = imageView.getHeight();
        if(height <= 0) {
            height = lp.height;
        }
        if(height <= 0) {
            height = imageView.getMaxHeight();
        }
        if(height <= 0) {
            height = displayMetrics.heightPixels;
        }

        imageViewSize.width = width;
        imageViewSize.height = height;

        return  imageViewSize;
    }

    /**
     * 添加Task到任务队列
     * @param runnable
     */
    private synchronized void addTask(Runnable runnable) {
        mTaskQueue.add(runnable);
        //当mPoolThreadHandler为空时，先等待mPoolThreadHandler初始化完成
        if(null == mPoolThreadHandler) {
            try {
                mSemaphorePoolThreadHandler.acquire();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
        mPoolThreadHandler.sendEmptyMessage(0x001);
    }

    /**
     * 从任务队列中获取任务
     * @return
     */
    private Runnable getTask() {
        if(mDispType == DispType.FIFO) {
            return mTaskQueue.removeFirst();
        } else if(mDispType == DispType.LIFO) {
            return mTaskQueue.removeLast();
        }

        return null;
    }

    /**
     * 根据key，在LruCache中获取Bitmap
     * @param path
     * @return
     */
    private Bitmap getBitmapFromLruCache(String path) {
        return mLruCache.get(path);
    }

    private class ImageBeanHolder {
        public String path;
        public ImageView imageView;
        public Bitmap bitmap;
    }

    /**
     * 单例模式，获取ImageLoader的实例
     * @return
     */
    public static ImageLoader getInstance() {
        if(null == mInstance) {
            synchronized (ImageLoader.class) {
                if(null == mInstance) {
                    mInstance = new ImageLoader(DEFAULT_THREAD_COUNT,DispType.LIFO);
                }
            }
        }

        return mInstance;
    }

    /**
     * 单例模式，获取ImageLoader的实例
     * @return
     */
    public static ImageLoader getInstance(int threadCount,DispType disType) {
        if(null == mInstance) {
            synchronized (ImageLoader.class) {
                if(null == mInstance) {
                    mInstance = new ImageLoader(threadCount,disType);
                }
            }
        }

        return mInstance;
    }

}
