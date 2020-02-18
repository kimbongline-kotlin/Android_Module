package com.kbline.kotlin_module.InstagramTagItem;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.RotateDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;

import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import java.util.ArrayList;

import bsc2086.kotlin_module.R;

public class InstaTag extends RelativeLayout {

    private int mPosX;
    private int mPosY;
    private int mRootWidth;
    private int mRootHeight;

    private Context mContext;

    private int mTagTextColor;
    private int mTagBackgroundColor;



    private boolean tagsAreAdded;
    private boolean canWeAddTags = true;
    private boolean showAllCarrots;
    private boolean mIsRootIsInTouch = true;

    private Animation mShowAnimation;
    private Animation mHideAnimation;

    private GestureDetector mGestureDetector;

    private TaggedImageEvent mTaggedImageEvent;

    private ViewGroup mRoot;

    public TextView mTagTxtViwe;
    private TagImageView mTagImageView;
    private final ArrayList<View> mTagList = new ArrayList<>();

    private final Runnable mSetRootHeightWidth = new Runnable() {
        @Override
        public void run() {
            mRootWidth = mRoot.getWidth();
            mRootHeight = mRoot.getHeight();

        }
    };

    private final OnTouchListener mTagOnTouchListener = new OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            return mGestureDetector.onTouchEvent(event);
        }
    };

    private final TagGestureListener mTagGestureListener = new TagGestureListener() {
        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            if (canWeAddTags) {
                if (mIsRootIsInTouch) {
                    int x = (int) e.getX();
                    int y = (int) e.getY();
                    switch (e.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                        case MotionEvent.ACTION_MOVE:
                        case MotionEvent.ACTION_UP:
                    }
                    if (mTaggedImageEvent != null) {
                        mTaggedImageEvent.singleTapConfirmedAndRootIsInTouch(x, y);
                    }
                } else {
                    hideRemoveButtonFromAllTagView();
                    mIsRootIsInTouch = true;
                }
            }
            return false;
        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            return true;
        }

        @Override
        public void onShowPress(MotionEvent e) {
        }

        @Override
        public boolean onDoubleTap(MotionEvent e) {
            if (mTaggedImageEvent != null) {
                return mTaggedImageEvent.onDoubleTap(e);
            } else {
                return true;
            }
        }

        @Override
        public boolean onDoubleTapEvent(MotionEvent e) {
            if (mTaggedImageEvent != null) {
                return mTaggedImageEvent.onDoubleTapEvent(e);
            } else {
                return true;
            }
        }

        @Override
        public void onLongPress(MotionEvent e) {
            if (mTaggedImageEvent != null) {
                mTaggedImageEvent.onLongPress(e);
            }
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            return true;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            return true;
        }
    };

    public interface Constants {
        int DEFAULT_COLOR = Color.WHITE;
        int TAG_TEXT_COLOR = Color.BLACK;

        String ANGLE_TOP = "ANGLE_TOP";
        String ANGLE_LEFT = "ANGLE_LEFT";
        String ANGLE_RIGHT = "ANGLE_RIGHT";
        String ANGLE_BOTTOM = "ANGLE_BOTTOM";
    }

    public interface TaggedImageEvent {
        void singleTapConfirmedAndRootIsInTouch(int x, int y);

        boolean onDoubleTap(MotionEvent e);

        boolean onDoubleTapEvent(MotionEvent e);

        void onLongPress(MotionEvent e);
    }

    public InstaTag(Context context) {
        super(context);
        initViewWithId(context);
    }

    public InstaTag(Context context, AttributeSet attrs) {
        super(context, attrs);
        if (!isInEditMode()) {
            initView(attrs, context);
        } else {
            initView(attrs, context);
        }
    }

    private void initViewWithId(Context context) {
        mContext = context;

        LayoutInflater inflater =
                (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.tag_root, this);

        mRoot = findViewById(R.id.tag_root);
        mTagImageView = findViewById(R.id.tag_image_view);
        setLayoutParamsToBeSetForRootLayout(mContext);
        mRoot.post(mSetRootHeightWidth);
        mRoot.setOnTouchListener(mTagOnTouchListener);
        mGestureDetector = new GestureDetector(mRoot.getContext(), mTagGestureListener);

    }

    private void initView(AttributeSet attrs, Context context) {
        mContext = context;

        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attrs,
                R.styleable.InstaTag, 0, 0);

        mTagBackgroundColor = obtainStyledAttributes.
                getColor(R.styleable.InstaTag_tagBackColor,Constants.DEFAULT_COLOR);



        mTagTextColor = obtainStyledAttributes.
                getColor(R.styleable.InstaTag_tagTextColor, Constants.TAG_TEXT_COLOR);

        int overrideDefaultColor = obtainStyledAttributes.
                getColor(R.styleable.InstaTag_overrideDefaultColor, Constants.DEFAULT_COLOR);

        if (overrideDefaultColor == Constants.DEFAULT_COLOR) {

            mTagBackgroundColor = obtainStyledAttributes.
                    getColor(R.styleable.InstaTag_tagBackColor, Constants.DEFAULT_COLOR);
        } else {
            mTagBackgroundColor = overrideDefaultColor;

        }

        initViewWithId(context);
        obtainStyledAttributes.recycle();
    }

    private void setCarrotVisibility(View tagView, String carrotType) {
        if (!showAllCarrots) {
            switch (carrotType) {
                case Constants.ANGLE_TOP:
                    tagView.findViewById(R.id.angle_top).setVisibility(View.VISIBLE);

                    tagView.findViewById(R.id.angle_left).setVisibility(View.INVISIBLE);
                    tagView.findViewById(R.id.angle_right).setVisibility(View.INVISIBLE);
                    tagView.findViewById(R.id.angle_bottom).setVisibility(View.INVISIBLE);
                    break;
                case Constants.ANGLE_LEFT:
                    tagView.findViewById(R.id.angle_left).setVisibility(View.VISIBLE);

                    tagView.findViewById(R.id.angle_top).setVisibility(View.INVISIBLE);
                    tagView.findViewById(R.id.angle_right).setVisibility(View.INVISIBLE);
                    tagView.findViewById(R.id.angle_bottom).setVisibility(View.INVISIBLE);
                    break;
                case Constants.ANGLE_RIGHT:
                    tagView.findViewById(R.id.angle_right).setVisibility(View.VISIBLE);

                    tagView.findViewById(R.id.angle_top).setVisibility(View.INVISIBLE);
                    tagView.findViewById(R.id.angle_left).setVisibility(View.INVISIBLE);
                    tagView.findViewById(R.id.angle_bottom).setVisibility(View.INVISIBLE);
                    break;
                case Constants.ANGLE_BOTTOM:
                    tagView.findViewById(R.id.angle_bottom).setVisibility(View.VISIBLE);

                    tagView.findViewById(R.id.angle_top).setVisibility(View.INVISIBLE);
                    tagView.findViewById(R.id.angle_left).setVisibility(View.INVISIBLE);
                    tagView.findViewById(R.id.angle_right).setVisibility(View.INVISIBLE);
                    break;
            }
        }
    }

    private void actionTagMove(View tagView, int pointerCount, int X, int Y) {
        int width = tagView.getWidth();
        int height = tagView.getHeight();
        int x = (int) tagView.getX();
        int y = (int) tagView.getY();

        if (x <= width && y <= height) {
            carrotType(Constants.ANGLE_TOP, tagView, pointerCount, X, mPosX, Y, mPosY);
        } else if (x + width >= mRootWidth && y + height >= mRootHeight) {
            carrotType(Constants.ANGLE_BOTTOM, tagView, pointerCount, X, mPosX, Y, mPosY);
        } else if (x - width <= 0 && y + height >= mRootHeight) {
            carrotType(Constants.ANGLE_BOTTOM, tagView, pointerCount, X, mPosX, Y, mPosY);
        } else if (x + width >= mRootWidth && y <= height / 2) {
            carrotType(Constants.ANGLE_TOP, tagView, pointerCount, X, mPosX, Y, mPosY);
        } else if (x <= 0 && y > height && y + height < mRootHeight) {
            carrotType(Constants.ANGLE_LEFT, tagView, pointerCount, X, mPosX, Y, mPosY);
        } else if (x > width && x + width < mRootWidth && y - height <= 0) {
            carrotType(Constants.ANGLE_TOP, tagView, pointerCount, X, mPosX, Y, mPosY);
        } else if (x + width >= mRootWidth && y > height && y + height < mRootHeight) {
            carrotType(Constants.ANGLE_RIGHT, tagView, pointerCount, X, mPosX, Y, mPosY);
        } else if (x > width && x + width < mRootWidth && y + height >= mRootHeight) {
            carrotType(Constants.ANGLE_BOTTOM, tagView, pointerCount, X, mPosX, Y, mPosY);
        } else {
            carrotType(Constants.ANGLE_TOP, tagView, pointerCount, X, mPosX, Y, mPosY);
        }
    }

    private void carrotType(String carrotType,
                            View tagView,
                            int pointerCount, int X, int posX, int Y, int posY) {
        switch (carrotType) {
            case Constants.ANGLE_TOP:
                setCarrotVisibility(tagView, Constants.ANGLE_TOP);
                setLayoutParamsForTagView(Constants.ANGLE_TOP,
                        pointerCount, X, posX, Y, posY, tagView);
                break;
            case Constants.ANGLE_LEFT:
                setCarrotVisibility(tagView, Constants.ANGLE_LEFT);
                setLayoutParamsForTagView(Constants.ANGLE_LEFT,
                        pointerCount, X, posX, Y, posY, tagView);
                break;
            case Constants.ANGLE_RIGHT:
                setCarrotVisibility(tagView, Constants.ANGLE_RIGHT);
                setLayoutParamsForTagView(Constants.ANGLE_RIGHT,
                        pointerCount, X, posX, Y, posY, tagView);
                break;
            case Constants.ANGLE_BOTTOM:
                setCarrotVisibility(tagView, Constants.ANGLE_BOTTOM);
                setLayoutParamsForTagView(Constants.ANGLE_BOTTOM,
                        pointerCount, X, posX, Y, posY, tagView);
                break;
        }
    }

    private void setLayoutParamsForTagView(String carrotType,
                                           int pointerCount, int X, int posX, int Y, int posY,
                                           View tagView) {
        int left = X - posX;
        int top = Y - posY;

        if (left < 0) {
            left = 0;
        } else if (left + tagView.getWidth() > mRootWidth) {
            left = mRootWidth - tagView.getWidth();
        }

        if (top < 0) {
            top = 0;
        } else if (top + tagView.getHeight() > mRootHeight) {
            top = mRootHeight - tagView.getHeight();
        }

        LayoutParams tagViewLayoutParams =
                (LayoutParams) tagView.getLayoutParams();
        if (pointerCount == 1) {
            switch (carrotType) {
                case Constants.ANGLE_TOP:
                case Constants.ANGLE_LEFT:
                case Constants.ANGLE_RIGHT:
                case Constants.ANGLE_BOTTOM:
                    tagViewLayoutParams.setMargins(left, top, 0, 0);
                    tagView.setLayoutParams(tagViewLayoutParams);
                    break;
            }
        }
    }


    private void setColorForTag(View tagView) {
        ((TextView) tagView.findViewById(R.id.tag_text_view)).setTextColor(this.mTagTextColor);

        setColor(tagView.findViewById(R.id.angle_top).getBackground(), this.mTagBackgroundColor);
        setColor(tagView.findViewById(R.id.angle_left).getBackground(), this.mTagBackgroundColor);
        setColor(tagView.findViewById(R.id.angle_right).getBackground(), this.mTagBackgroundColor);
        setColor(tagView.findViewById(R.id.angle_bottom).getBackground(), this.mTagBackgroundColor);
        setColor(tagView.findViewById(R.id.tag_text_container).getBackground(), this.mTagBackgroundColor);
    }

    private void hideRemoveButtonFromAllTagView() {
        if (!mTagList.isEmpty()) {
            for (View view : mTagList) {
                view.findViewById(R.id.remove_tag_image_view).setVisibility(View.GONE);
            }
        }
    }

    private boolean tagNotTaggedYet(String tagName) {
        boolean tagFound = true;
        if (!mTagList.isEmpty()) {
            for (View tagView : mTagList) {
                if (((TextView) tagView.
                        findViewById(R.id.tag_text_view)).
                        getText().toString().equals(tagName)) {
                    tagFound = false;
                    break;
                }
            }
        } else {
            tagFound = true;
        }
        return tagFound;
    }

    private void setColor(Drawable drawable, int color) {
        if (drawable instanceof ShapeDrawable) {
            ((ShapeDrawable) drawable).getPaint().setColor(color);
        } else if (drawable instanceof GradientDrawable) {
            ((GradientDrawable) drawable).setColor(color);
        } else if (drawable instanceof ColorDrawable) {
            ((ColorDrawable) drawable).setColor(color);
        } else if (drawable instanceof LayerDrawable) {
            LayerDrawable layerDrawable = (LayerDrawable) drawable;
            RotateDrawable rotateDrawable =
                    (RotateDrawable) layerDrawable.findDrawableByLayerId(R.id.carrot_shape_top);
            setColor(rotateDrawable.getDrawable(), color);
        } else if (drawable instanceof RotateDrawable) {
            setColor(((RotateDrawable) drawable).getDrawable(), color);
        }
    }

    private void setLayoutParamsToBeSetForRootLayout(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        int rootLayoutHeightWidth = (int) TypedValue.applyDimension(1, ((float) displayMetrics.widthPixels) / displayMetrics.density, getResources().getDisplayMetrics());
        ViewGroup.LayoutParams params = this.mRoot.getLayoutParams();
        params.height = rootLayoutHeightWidth;
        params.width = rootLayoutHeightWidth;
        this.mRoot.setLayoutParams(params);
    }

    private int getXWhileAddingTag(Double x) {

        return (int) (this.mRootWidth *  x);
    }

    private int getYWhileAddingTag(Double y) {
        return (int) (this.mRootWidth *  y);
    }

    public void addTag(int x, int y, String tagText, String tagInfo) {
        if (tagNotTaggedYet(tagText)) {

            LayoutInflater layoutInflater = LayoutInflater.from(mContext);

            final View tagView = layoutInflater.
                    inflate(R.layout.insta_tag_view, mRoot, false);
            final TextView tagTextView = tagView.findViewById(R.id.tag_text_view);
            mTagTxtViwe = tagTextView;
            mTagTxtViwe.setText(tagText);
            mTagTxtViwe.setTag(String.valueOf(tagInfo));

            final LinearLayout carrotTopContainer =
                    tagView.findViewById(R.id.angle_top);
            final LinearLayout carrotLeftContainer =
                    tagView.findViewById(R.id.angle_left);
            final LinearLayout carrotRightContainer =
                    tagView.findViewById(R.id.angle_right);
            final LinearLayout carrotBottomContainer =
                    tagView.findViewById(R.id.angle_bottom);

            final ImageView removeTagImageView =
                    tagView.findViewById(R.id.remove_tag_image_view);
            final LinearLayout textContainer =
                    tagView.findViewById(R.id.tag_text_container);

//            if (mTagTextDrawable != null) {
//                ViewCompat.setBackground(textContainer, mTagTextDrawable);
//            }
//            if (mCarrotTopDrawable != null) {
//                ViewCompat.setBackground(carrotTopContainer, mCarrotTopDrawable);
//            }
//            if (mCarrotLeftDrawable != null) {
//                ViewCompat.setBackground(carrotLeftContainer, mCarrotLeftDrawable);
//            }
//            if (mCarrotRightDrawable != null) {
//                ViewCompat.setBackground(carrotRightContainer, mCarrotRightDrawable);
//            }
//            if (mCarrotBottomDrawable != null) {
//                ViewCompat.setBackground(carrotBottomContainer, mCarrotBottomDrawable);
//            }

            if (showAllCarrots) {
                tagView.findViewById(R.id.angle_top).setVisibility(View.VISIBLE);
                tagView.findViewById(R.id.angle_left).setVisibility(View.VISIBLE);
                tagView.findViewById(R.id.angle_right).setVisibility(View.VISIBLE);
                tagView.findViewById(R.id.angle_bottom).setVisibility(View.VISIBLE);
            }

            tagTextView.setText(tagText);
            tagTextView.setTag(String.valueOf(tagInfo));
            setColorForTag(tagView);

            LayoutParams layoutParams =
                    new LayoutParams(
                            LayoutParams.WRAP_CONTENT,
                            LayoutParams.WRAP_CONTENT);
            layoutParams.
                    setMargins(x - (tagView.getLayoutParams().width / 2),
                            y - (tagView.getLayoutParams().height / 2),
                            0,
                            0);
            tagView.setLayoutParams(layoutParams);

            mTagList.add(tagView);
            mRoot.addView(tagView);

            removeTagImageView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    canWeAddTags = true;
                    mTagList.remove(tagView);
                    mRoot.removeView(tagView);
                }
            });


            tagView.setOnTouchListener(new OnTouchListener() {
                public boolean onTouch(final View view, MotionEvent event) {

                    if (canWeAddTags) {
                        mIsRootIsInTouch = false;

                        final int X = (int) event.getRawX();
                        final int Y = (int) event.getRawY();

                        int pointerCount = event.getPointerCount();

                        switch (event.getAction() & MotionEvent.ACTION_MASK) {
                            case MotionEvent.ACTION_DOWN:
                                LayoutParams layoutParams =
                                        (LayoutParams) view.getLayoutParams();

                                mPosX = X - layoutParams.leftMargin;
                                mPosY = Y - layoutParams.topMargin;

                                removeTagImageView.setVisibility(View.VISIBLE);
                                break;
                            case MotionEvent.ACTION_UP:
                                break;
                            case MotionEvent.ACTION_POINTER_DOWN:
                                break;
                            case MotionEvent.ACTION_POINTER_UP:
                                break;
                            case MotionEvent.ACTION_MOVE:
                                actionTagMove(tagView, pointerCount, X, Y);
                                break;
                        }
                        mRoot.invalidate();
                    }
                    return true;
                }
            });
        } else {

        }
    }

    public void addTagViewFromTagsToBeTagged(ArrayList<TagToBeTagged> tagsToBeTagged) {
        if (!tagsAreAdded) {
            canWeAddTags = false;
            for (TagToBeTagged tagToBeTagged : tagsToBeTagged) {
                addTag(getXWhileAddingTag(tagToBeTagged.getX_co_ord()),
                        getYWhileAddingTag(tagToBeTagged.getY_co_ord()),
                        tagToBeTagged.getTitle(),tagToBeTagged.getInfo());
            }
            tagsAreAdded = true;
        }
    }

    public TagImageView getTagImageView() {
        return mTagImageView;
    }

    public ArrayList<TagToBeTagged> getListOfTagsToBeTagged() {
        ArrayList<TagToBeTagged> tagsToBeTagged = new ArrayList<>();

        if (!mTagList.isEmpty()) {
            for (int i = 0; i < mTagList.size(); i++) {
                View view = mTagList.get(i);
                tagsToBeTagged.
                        add(new TagToBeTagged(((TextView) view.
                                findViewById(R.id.tag_text_view)).getText().toString(),((TextView) view.
                                findViewById(R.id.tag_text_view)).getTag().toString(), Double.valueOf(((double) view.getX()) / ((double) this.mRootWidth)), Double.valueOf(((double) view.getY()) / ((double) this.mRootHeight))));
            }
        }
        return tagsToBeTagged;
    }

    public void setImageToBeTaggedEvent(TaggedImageEvent taggedImageEvent) {
        if (this.mTaggedImageEvent == null) {
            this.mTaggedImageEvent = taggedImageEvent;
        }
    }

    public void showTags() {
        if (!mTagList.isEmpty()) {
            for (View tagView : mTagList) {
                tagView.setVisibility(VISIBLE);
                tagView.startAnimation(mShowAnimation);
            }
        }
    }

    public void hideTags() {
        if (!mTagList.isEmpty()) {
            for (View tagView : mTagList) {
                tagView.startAnimation(mHideAnimation);
                tagView.setVisibility(GONE);
            }
        }
    }

    public void removeTags() {
        if (!mTagList.isEmpty()) {
            for (View tagView : mTagList) {
                mRoot.removeView(tagView);
            }
            mTagList.clear();
        }
    }


    public TextView getTextView() {
        return this.mTagTxtViwe;
    }

    public int getRootWidth() {
        return mRootWidth;
    }

    public void setRootWidth(int mRootWidth) {
        this.mRootWidth = mRootWidth;
    }

    public int getRootHeight() {
        return mRootHeight;
    }

    public void setRootHeight(int mRootHeight) {
        this.mRootWidth = mRootHeight;
        this.mRootHeight = mRootHeight;
    }

    public int getTagTextColor() {
        return mTagTextColor;
    }

    public int getTagBackgroundColor() {
        return mTagBackgroundColor;
    }

    public boolean canWeAddTags() {
        return canWeAddTags;
    }

    public void setCanWeAddTags(boolean mCanWeAddTags) {
        this.canWeAddTags = mCanWeAddTags;
    }

    public boolean isShowAllCarrots() {
        return showAllCarrots;
    }

    public float convertDpToPixel(float dp, Context context) {
        return (((float) context.getResources().getDisplayMetrics().densityDpi) / DisplayMetrics.DENSITY_DEFAULT) * dp;
    }

    public float convertPixelsToDp(float px, Context context) {
        return px / (((float) context.getResources().getDisplayMetrics().densityDpi) / DisplayMetrics.DENSITY_DEFAULT);
    }


}