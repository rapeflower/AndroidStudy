一、Java多线程
1.线程的同步和异步
  1）同步：A线程要请求某个资源，但是此资源正在被B线程使用中，因为同步机制存在，A线程请求不到，此时A线程就只能等待下去。
  2）异步：A线程要请求某个资源，但是此资源正在被B线程使用中，因为没有同步机制的存在，A线程仍然请求的到，A线程无需等待。
  显然，同步最安全，最保险的，而异步不安全，容易导致死锁。
  3）java同步机制有4种实现方式
     ThreadLocal
     synchronized()
     wait()与notify() [都是Object的方法]
     volatile
  4）sleep()和wait()
     sleep是线程类（Thread）的方法，导致此线程执行指定时间，把执行机会给其它线程，但是监控状态依然保持，到达指定时间后会自动恢复。
     调用sleep不会释放锁对象。
     wait是Object类的方法，对此对象调用wait方法导致本线程放弃对象锁，进入等待此对象的等待锁定池，只有针对此对象发出notify方法（或notifyAll）
     后本线程才进入对象锁定池准备获得对象锁进入运行状态。

2.线程的并行
3.线程的并发
4.ThreadLocal
5.集合
  Collection
    -List
        -ArrayList
        -LinkedList
        -Vector
    -Set
        -HashSet
        -TreeSet

  List
  1）List有序且可重复，子类ArrayList也是有序可重复。
  2）LinkedList是一个双向链表结构的。

  Set
  1）Set及Set的实现类都是无序且不可重复。

  Map
    -AbstractMap
        -HashMap
        -TreeMap
  1）Map也是接口，该接口描述了从不重复的键到值的映射。
  2）HashMap，中文叫散列表，基于哈希表实现，特点就是键值对的映射关系。HashMap中元素的排列顺序是不固定的，更加适合于对元素进行插入、删除和定位。
  3）TreeMap，基于红黑树实现，TreeMap中的元素保持着某种固定的顺序。更加适合于对元素顺序遍历。

二、Android
1.android只能在主线程中更新UI？
  Android UI操作并不是线程安全的，并且这些操作必须在UI线程执行。

2.android应用自启动和保活（应用生态圈）
3.android布局优化
  <include/>
  <merge/>
  <ViewStub/>
  <Space/>
4.android图片加载
  Image-Loader, Glide, Picasso, Fresco
5.svg
6.AnimationDrawable性能有点欠缺
7.WindowManager
8.DecorView
9.android事件分发机制
  1）dispatchTouchEvent-分发事件，默认为false。true：取消事件，不继续向下分发，false：向下分发事件。
  2）onInterceptTouchEvent-拦截事件，默认为false。true：拦截事件，自身的onTouchEvent()方法消费，false：事件继续向下传递。
  3）onTouchEvent-处理事件，默认为false，true：消费事件，false：不消费事件，向上层传递让上层处理。【注意】如果发生了拦截，那么如果该层不处理则会继续向上
   传递，让上层处理。如果过程中没有发生处理，则事件分发到底层后将一直向上层传递到Activity，在Activity的onTouchEvent()中处理。【注意】如果在设置了
   setOnclickListener(...)的View或ViewGroup中，返回true则消费事件，会触发onClick事件，如果返回false，则不会触发onClick事件。
10.android View的绘制流程
   measure过程
   layout过程
   draw过程
11.
    double base_scale = Math.round(mScale * 100) * 0.01d;
    if (base_scale == 1) {
        ViewHelper.setAlpha(tv_name, 1.0f);
        tv_name.setTextColor(tabTextColor);
    } else {
        ViewHelper.setAlpha(tv_name, positionOffset * 1.0f);
    }
12.


