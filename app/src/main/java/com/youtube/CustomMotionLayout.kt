package com.youtube

import android.content.Context
import android.graphics.Rect
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import androidx.constraintlayout.motion.widget.MotionLayout

// 모션레이아웃 뒤에있는 영상목록(리사이클러뷰)이 터치가 안됨(메인엑티비티 영역)
// why?? 리사이클러뷰 앞에있는 프레임레이아웃의 프래그먼트(모션레이아웃 영역)가 터치를 다 가져감
// 때문에 커스텀 모션레이아웃을 만들어서 모션레이아웃이 적용되는 영역(mainContainerView)
// 이 터치가 된 경우에만 모션레이아웃이 동작하도록 하고 그 바깥 영역을 터치했을 때는
// 모션레이아웃의 터치이벤트를 흘려보내도록 하고 뒤의 영역들이 터치가 가능하도록 한다.
class CustomMotionLayout(context: Context, attributeSet: AttributeSet? = null): MotionLayout(context, attributeSet) {

    private var motionTouchStarted = false // 정확한 영역 눌렀을 때 터치이벤트(드래그)를 모션레이아웃이 갖게하기
    private val mainContainerView by lazy {
        findViewById<View>(R.id.mainContainerLayout)
    }
    private val hitRect = Rect()

    init {
        setTransitionListener(object: TransitionListener {
            override fun onTransitionStarted(p0: MotionLayout?, p1: Int, p2: Int) {}
            override fun onTransitionChange(p0: MotionLayout?, p1: Int, p2: Int, p3: Float) {}
            override fun onTransitionCompleted(p0: MotionLayout?, p1: Int) {
                motionTouchStarted = false
            }
            override fun onTransitionTrigger(p0: MotionLayout?, p1: Int, p2: Boolean, p3: Float) {}
        })
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when(event.actionMasked){
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                motionTouchStarted = false
                return super.onTouchEvent(event)
            }
        }

        if(!motionTouchStarted) {
            mainContainerView.getHitRect(hitRect)
            motionTouchStarted = hitRect.contains(event.x.toInt(), event.y.toInt())
        }

        return super.onTouchEvent(event) && motionTouchStarted
    }

    private val gestureListener by lazy {
        object: GestureDetector.SimpleOnGestureListener(){
            override fun onScroll(
                e1: MotionEvent,
                e2: MotionEvent,
                distanceX: Float,
                distanceY: Float
            ): Boolean {
                mainContainerView.getHitRect(hitRect)
                return hitRect.contains(e1.x.toInt(), e2.y.toInt())
            }
        }
    }

    private val gestureDetector by lazy {
        GestureDetector(context, gestureListener)
    }

    override fun onInterceptTouchEvent(event: MotionEvent?): Boolean {
        return gestureDetector.onTouchEvent(event)
    }

}