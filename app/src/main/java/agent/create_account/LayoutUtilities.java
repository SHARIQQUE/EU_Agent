package agent.create_account;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import agent.activities.R;

/**
 * Created by Estel_WP on 11-Sep-18.
 */

public class LayoutUtilities {

    private Context context;
    TextView textView;

    public View getMiscellinousView() {
        return miscellinousView;
    }

    public LinearLayout getParentLL() {
        return parentLL;
    }

    public void setMiscellinousView(View miscellinousView) {
        this.miscellinousView = miscellinousView;
    }

    private int height, width;
    private VIEWTEMPLATE viewtemplate;
    private View miscellinousView;

    private LinearLayout parentLL, child_One_LL,
            child_Two_LL,
            child_Three_LL,
            child_Four_LL,
            horizontal_One_LL,
            horizontal_Two_LL,
            vertical_One_LL,
            header_one_LL,
            header_two_LL,
            vertical_Two_LL;

    LayoutUtilities(Context context, int height, int width) {

        this.context = context;
        this.height = height;
        this.width = width;

    }

    LayoutUtilities(Context context) {

        this.context = context;


    }

    public void initiateViewForImage_Restrict() {

        parentLL = new LinearLayout(context);

        LinearLayout.LayoutParams paramMain = new LinearLayout.LayoutParams(width, height);
//        paramMain.height=height;
//        paramMain.width=width;
        parentLL.setLayoutParams(paramMain);

        parentLL.setBackgroundColor(Color.parseColor("#ffffff"));

        viewtemplate=VIEWTEMPLATE.MISCELLINOUS;

    }

    public void initiateViewForImage(VIEWTEMPLATE viewtemplate) {

        parentLL = new LinearLayout(context);

        LinearLayout.LayoutParams paramMain = new LinearLayout.LayoutParams(width, height);
//        paramMain.height=height;
//        paramMain.width=width;
        parentLL.setLayoutParams(paramMain);

        parentLL.setBackgroundColor(Color.parseColor("#ffffff"));


        LinearLayout.LayoutParams params;

        this.viewtemplate = viewtemplate;
        switch (viewtemplate) {

            case THREE_BLOCK:

                // first layout
                parentLL.setOrientation(LinearLayout.HORIZONTAL);


                child_One_LL = new LinearLayout(context);
                child_One_LL.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
                child_One_LL.setOrientation(LinearLayout.VERTICAL);
                child_One_LL.setBackgroundColor(Color.parseColor("#ffffff"));
                params = (LinearLayout.LayoutParams) child_One_LL.getLayoutParams(); //or create new LayoutParams...
                params.weight = 1.0f;


                // second layout
                child_Two_LL = new LinearLayout(context);
                child_Two_LL.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0));
                child_Two_LL.setOrientation(LinearLayout.VERTICAL);
                child_Two_LL.setBackgroundColor(Color.parseColor("#ffffff"));
                params = (LinearLayout.LayoutParams) child_Two_LL.getLayoutParams(); //or create new LayoutParams...
                child_Two_LL.setWeightSum(1);
                params.weight = 0.5f;


                // three layout
                child_Three_LL = new LinearLayout(context);
                child_Three_LL.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0));
                child_Three_LL.setOrientation(LinearLayout.VERTICAL);
                child_Three_LL.setBackgroundColor(Color.parseColor("#ffffff"));
                params = (LinearLayout.LayoutParams) child_Three_LL.getLayoutParams(); //or create new LayoutParams...
                params.weight = 0.5f;


                // vertical one

                vertical_One_LL = new LinearLayout(context);
                vertical_One_LL.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT));
                vertical_One_LL.setOrientation(LinearLayout.VERTICAL);
                vertical_One_LL.setBackgroundColor(Color.parseColor("#000000"));
                params = (LinearLayout.LayoutParams) vertical_One_LL.getLayoutParams(); //or create new LayoutParams...
                params.weight = 0.5f;


                vertical_Two_LL = new LinearLayout(context);
                vertical_Two_LL.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT));
                vertical_Two_LL.setOrientation(LinearLayout.VERTICAL);
                vertical_Two_LL.setBackgroundColor(Color.parseColor("#000000"));
                params = (LinearLayout.LayoutParams) vertical_Two_LL.getLayoutParams(); //or create new LayoutParams...
                params.weight = 0.5f;
//                finaliseViewForImage();

                break;

            case THREE_BLOCK_HEADER_ONE:

                // first layout
                parentLL.setOrientation(LinearLayout.VERTICAL);


                child_One_LL = new LinearLayout(context);
                child_One_LL.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
                child_One_LL.setOrientation(LinearLayout.VERTICAL);
                child_One_LL.setBackgroundColor(Color.parseColor("#ffffff"));
                params = (LinearLayout.LayoutParams) child_One_LL.getLayoutParams(); //or create new LayoutParams...
                params.weight = 1.0f;

                header_one_LL = new LinearLayout(context);
                header_one_LL.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0));
                header_one_LL.setOrientation(LinearLayout.HORIZONTAL);
                header_one_LL.setBackgroundColor(Color.parseColor("#ffffff"));
                params = (LinearLayout.LayoutParams) header_one_LL.getLayoutParams(); //or create new LayoutParams...
                params.weight = 0.2f;

                horizontal_One_LL = new LinearLayout(context);
                horizontal_One_LL.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0));
                horizontal_One_LL.setOrientation(LinearLayout.HORIZONTAL);
                horizontal_One_LL.setBackgroundColor(Color.parseColor("#ffffff"));
                params = (LinearLayout.LayoutParams) horizontal_One_LL.getLayoutParams(); //or create new LayoutParams...
                params.weight = 0.8f;

                // second layout
                child_Two_LL = new LinearLayout(context);
                child_Two_LL.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0));
                child_Two_LL.setOrientation(LinearLayout.VERTICAL);
                child_Two_LL.setBackgroundColor(Color.parseColor("#ffffff"));
                params = (LinearLayout.LayoutParams) child_Two_LL.getLayoutParams(); //or create new LayoutParams...
                child_Two_LL.setWeightSum(1);
                params.weight = 0.5f;


                // three layout
                child_Three_LL = new LinearLayout(context);
                child_Three_LL.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0));
                child_Three_LL.setOrientation(LinearLayout.VERTICAL);
                child_Three_LL.setBackgroundColor(Color.parseColor("#ffffff"));
                params = (LinearLayout.LayoutParams) child_Three_LL.getLayoutParams(); //or create new LayoutParams...
                params.weight = 0.5f;


                // vertical one

                vertical_One_LL = new LinearLayout(context);
                vertical_One_LL.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT));
                vertical_One_LL.setOrientation(LinearLayout.VERTICAL);
                vertical_One_LL.setBackgroundColor(Color.parseColor("#000000"));
                params = (LinearLayout.LayoutParams) vertical_One_LL.getLayoutParams(); //or create new LayoutParams...
                params.weight = 0.5f;


                vertical_Two_LL = new LinearLayout(context);
                vertical_Two_LL.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT));
                vertical_Two_LL.setOrientation(LinearLayout.VERTICAL);
                vertical_Two_LL.setBackgroundColor(Color.parseColor("#000000"));
                params = (LinearLayout.LayoutParams) vertical_Two_LL.getLayoutParams(); //or create new LayoutParams...
                params.weight = 0.5f;
//                finaliseViewForImage();

                break;


            case THREE_BLOCK_HEADER_TWO:

                // first layout
                parentLL.setOrientation(LinearLayout.VERTICAL);


                child_One_LL = new LinearLayout(context);
                child_One_LL.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
                child_One_LL.setOrientation(LinearLayout.VERTICAL);
                child_One_LL.setBackgroundColor(Color.parseColor("#ffffff"));
                params = (LinearLayout.LayoutParams) child_One_LL.getLayoutParams(); //or create new LayoutParams...
                params.weight = 1.0f;

                header_one_LL = new LinearLayout(context);
                header_one_LL.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0));
                header_one_LL.setOrientation(LinearLayout.HORIZONTAL);
                header_one_LL.setBackgroundColor(Color.parseColor("#ffffff"));
                params = (LinearLayout.LayoutParams) header_one_LL.getLayoutParams(); //or create new LayoutParams...
                params.weight = 0.1f;


                header_two_LL = new LinearLayout(context);
                header_two_LL.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0));
                header_two_LL.setOrientation(LinearLayout.VERTICAL);
                header_two_LL.setBackgroundColor(Color.parseColor("#ffffff"));
                params = (LinearLayout.LayoutParams) header_two_LL.getLayoutParams(); //or create new LayoutParams...
                params.weight = 0.2f;

                horizontal_One_LL = new LinearLayout(context);
                horizontal_One_LL.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0));
                horizontal_One_LL.setOrientation(LinearLayout.HORIZONTAL);
                horizontal_One_LL.setBackgroundColor(Color.parseColor("#ffffff"));
                params = (LinearLayout.LayoutParams) horizontal_One_LL.getLayoutParams(); //or create new LayoutParams...
                params.weight = 0.8f;

                // second layout
                child_Two_LL = new LinearLayout(context);
                child_Two_LL.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0));
                child_Two_LL.setOrientation(LinearLayout.VERTICAL);
                child_Two_LL.setBackgroundColor(Color.parseColor("#ffffff"));
                params = (LinearLayout.LayoutParams) child_Two_LL.getLayoutParams(); //or create new LayoutParams...
                child_Two_LL.setWeightSum(1);
                params.weight = 0.5f;


                // three layout
                child_Three_LL = new LinearLayout(context);
                child_Three_LL.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0));
                child_Three_LL.setOrientation(LinearLayout.VERTICAL);
                child_Three_LL.setBackgroundColor(Color.parseColor("#ffffff"));
                params = (LinearLayout.LayoutParams) child_Three_LL.getLayoutParams(); //or create new LayoutParams...
                params.weight = 0.5f;


                // vertical one

                vertical_One_LL = new LinearLayout(context);
                vertical_One_LL.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT));
                vertical_One_LL.setOrientation(LinearLayout.VERTICAL);
                vertical_One_LL.setBackgroundColor(Color.parseColor("#000000"));
                params = (LinearLayout.LayoutParams) vertical_One_LL.getLayoutParams(); //or create new LayoutParams...
                params.weight = 0.5f;


                vertical_Two_LL = new LinearLayout(context);
                vertical_Two_LL.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT));
                vertical_Two_LL.setOrientation(LinearLayout.VERTICAL);
                vertical_Two_LL.setBackgroundColor(Color.parseColor("#000000"));
                params = (LinearLayout.LayoutParams) vertical_Two_LL.getLayoutParams(); //or create new LayoutParams...
                params.weight = 0.5f;
//                finaliseViewForImage();

                break;

            case FOUR_BLOCK:
                // first layout
                parentLL.setOrientation(LinearLayout.HORIZONTAL);
                child_One_LL = new LinearLayout(context);
                child_One_LL.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0));
                child_One_LL.setOrientation(LinearLayout.VERTICAL);
                child_One_LL.setBackgroundColor(Color.parseColor("#ffffff"));
                params = (LinearLayout.LayoutParams) child_One_LL.getLayoutParams(); //or create new LayoutParams...
                params.weight = 0.5f;


                // second layout
                child_Two_LL = new LinearLayout(context);
                child_Two_LL.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0));
                child_Two_LL.setOrientation(LinearLayout.VERTICAL);
                child_Two_LL.setBackgroundColor(Color.parseColor("#ffffff"));
                params = (LinearLayout.LayoutParams) child_Two_LL.getLayoutParams(); //or create new LayoutParams...
                child_Two_LL.setWeightSum(1);
                params.weight = 0.5f;

                // three layout
                child_Three_LL = new LinearLayout(context);
                child_Three_LL.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0));
                child_Three_LL.setOrientation(LinearLayout.VERTICAL);
                child_Three_LL.setBackgroundColor(Color.parseColor("#ffffff"));
                params = (LinearLayout.LayoutParams) child_Three_LL.getLayoutParams(); //or create new LayoutParams...
                params.weight = 0.5f;


                // four layout
                child_Four_LL = new LinearLayout(context);
                child_Four_LL.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0));
                child_Four_LL.setOrientation(LinearLayout.VERTICAL);
                child_Four_LL.setBackgroundColor(Color.parseColor("#ffffff"));
                params = (LinearLayout.LayoutParams) child_Four_LL.getLayoutParams(); //or create new LayoutParams...
                params.weight = 0.5f;


                vertical_One_LL = new LinearLayout(context);
                vertical_One_LL.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT));
                vertical_One_LL.setOrientation(LinearLayout.VERTICAL);
                vertical_One_LL.setBackgroundColor(Color.parseColor("#000000"));
                params = (LinearLayout.LayoutParams) vertical_One_LL.getLayoutParams(); //or create new LayoutParams...
                params.weight = 0.5f;


                vertical_Two_LL = new LinearLayout(context);
                vertical_Two_LL.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT));
                vertical_Two_LL.setOrientation(LinearLayout.VERTICAL);
                vertical_Two_LL.setBackgroundColor(Color.parseColor("#000000"));
                params = (LinearLayout.LayoutParams) vertical_Two_LL.getLayoutParams(); //or create new LayoutParams...
                params.weight = 0.5f;


                break;


            case SINGLE_BLOCK:
                parentLL.setOrientation(LinearLayout.VERTICAL);
                child_One_LL = new LinearLayout(context);
                child_One_LL.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
                child_One_LL.setOrientation(LinearLayout.VERTICAL);
                child_One_LL.setBackgroundColor(Color.parseColor("#ffffff"));
//                params = (LinearLayout.LayoutParams) child_One_LL.getLayoutParams(); //or create new LayoutParams...
//                params.weight = 1.0f;

                vertical_One_LL = new LinearLayout(context);
                vertical_One_LL.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, height));
                vertical_One_LL.setOrientation(LinearLayout.VERTICAL);
                vertical_One_LL.setBackgroundColor(Color.parseColor("#ffffff"));

//                params= (LinearLayout.LayoutParams) vertical_One_LL.getLayoutParams(); //or create new LayoutParams...
//                params.weight = 1.0f;


                break;


            case TWO_BLOCK_VERTICAL:
                // first layout
                parentLL.setOrientation(LinearLayout.HORIZONTAL);


                child_One_LL = new LinearLayout(context);
                child_One_LL.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
                child_One_LL.setOrientation(LinearLayout.VERTICAL);
                child_One_LL.setBackgroundColor(Color.parseColor("#ffffff"));
                params = (LinearLayout.LayoutParams) child_One_LL.getLayoutParams(); //or create new LayoutParams...
                params.weight = 1.0f;


                // second layout
                child_Two_LL = new LinearLayout(context);
                child_Two_LL.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0));
                child_Two_LL.setOrientation(LinearLayout.VERTICAL);
                child_Two_LL.setBackgroundColor(Color.parseColor("#ffffff"));
                params = (LinearLayout.LayoutParams) child_Two_LL.getLayoutParams(); //or create new LayoutParams...
                child_Two_LL.setWeightSum(1);
                params.weight = 1.0f;

                // vertical one

                vertical_One_LL = new LinearLayout(context);
                vertical_One_LL.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT));
                vertical_One_LL.setOrientation(LinearLayout.VERTICAL);
                vertical_One_LL.setBackgroundColor(Color.parseColor("#ffffff"));
                params = (LinearLayout.LayoutParams) vertical_One_LL.getLayoutParams(); //or create new LayoutParams...
                params.weight = 0.5f;


                vertical_Two_LL = new LinearLayout(context);
                vertical_Two_LL.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT));
                vertical_Two_LL.setOrientation(LinearLayout.VERTICAL);
                vertical_Two_LL.setBackgroundColor(Color.parseColor("#ffffff"));
                params = (LinearLayout.LayoutParams) vertical_Two_LL.getLayoutParams(); //or create new LayoutParams...
                params.weight = 0.5f;

                break;

            case TWO_BLOCK_HORIZONTAL:

                parentLL.setOrientation(LinearLayout.VERTICAL);


                child_One_LL = new LinearLayout(context);
                child_One_LL.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
                child_One_LL.setOrientation(LinearLayout.VERTICAL);
                child_One_LL.setBackgroundColor(Color.parseColor("#ffffff"));
                params = (LinearLayout.LayoutParams) child_One_LL.getLayoutParams(); //or create new LayoutParams...
                params.weight = 1.0f;


                // second layout
                child_Two_LL = new LinearLayout(context);
                child_Two_LL.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0));
                child_Two_LL.setOrientation(LinearLayout.VERTICAL);
                child_Two_LL.setBackgroundColor(Color.parseColor("#ffffff"));
                params = (LinearLayout.LayoutParams) child_Two_LL.getLayoutParams(); //or create new LayoutParams...
                child_Two_LL.setWeightSum(1);
                params.weight = 1.0f;

                // vertical one

                vertical_One_LL = new LinearLayout(context);
                vertical_One_LL.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0));
                vertical_One_LL.setOrientation(LinearLayout.VERTICAL);
                vertical_One_LL.setBackgroundColor(Color.parseColor("#ffffff"));
                params = (LinearLayout.LayoutParams) vertical_One_LL.getLayoutParams(); //or create new LayoutParams...
                params.weight = 0.5f;


                vertical_Two_LL = new LinearLayout(context);
                vertical_Two_LL.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0));
                vertical_Two_LL.setOrientation(LinearLayout.VERTICAL);
                vertical_Two_LL.setBackgroundColor(Color.parseColor("#ffffff"));
                params = (LinearLayout.LayoutParams) vertical_Two_LL.getLayoutParams(); //or create new LayoutParams...
                params.weight = 0.5f;

                break;


        }


    }

    enum LayoutParams_LOCAL {

        BOTH_MATCH_PARENT,
        BOTH_WRAP_CONTENT,
        HEIGHT_MATCH_PARENT,
        WIDTH_MATCH_PARENT,
        SIZE,
        CUSTOM

    }

    ;

    enum VIEWTEMPLATE {

        FOUR_BLOCK,
        THREE_BLOCK,
        TWO_BLOCK_HORIZONTAL,
        TWO_BLOCK_VERTICAL,
        SINGLE_BLOCK,
        THREE_BLOCK_HEADER_ONE,
        THREE_BLOCK_HEADER_TWO,

        MISCELLINOUS

    }

    ;

    enum ViewType {

        TEXTVIEW,
        IMAGEVIEW,
        VIEW,
        TABLEVIEW

    }

    ;

    enum VIEWTEMPLATE_BLOCK {

        FIRST_BLOCK,
        SECOND_BLOCK,
        THIRD_BLOCK,
        FOURTH_BLOCK,
        HEADER_BLOCK_ONE,
        HEADER_BLOCK_TWO


    }

    ;


    public void buildViewForImage(View childView, VIEWTEMPLATE_BLOCK viewtemplate_block) {

        switch (viewtemplate) {

            case THREE_BLOCK:

                switch (viewtemplate_block) {

                    case FIRST_BLOCK:

                        child_One_LL.addView(childView);


                        break;

                    case SECOND_BLOCK:

                        child_Two_LL.addView(childView);

                        break;

                    case THIRD_BLOCK:

                        child_Three_LL.addView(childView);
                        break;


                }


                break;

            case THREE_BLOCK_HEADER_ONE:

                switch (viewtemplate_block) {

                    case FIRST_BLOCK:

                        child_One_LL.addView(childView);


                        break;

                    case SECOND_BLOCK:

                        child_Two_LL.addView(childView);

                        break;

                    case THIRD_BLOCK:

                        child_Three_LL.addView(childView);
                        break;

                    case HEADER_BLOCK_ONE:

                        header_one_LL.addView(childView);
                        break;


                }


                break;


            case THREE_BLOCK_HEADER_TWO:

                switch (viewtemplate_block) {

                    case FIRST_BLOCK:

                        child_One_LL.addView(childView);


                        break;

                    case SECOND_BLOCK:

                        child_Two_LL.addView(childView);

                        break;

                    case THIRD_BLOCK:

                        child_Three_LL.addView(childView);
                        break;

                    case HEADER_BLOCK_ONE:

                        header_one_LL.addView(childView);
                        break;

                    case HEADER_BLOCK_TWO:

                        header_two_LL.addView(childView);
                        break;


                }


                break;

            case FOUR_BLOCK:

                switch (viewtemplate_block) {

                    case FIRST_BLOCK:

                        child_One_LL.addView(childView);


                        break;

                    case SECOND_BLOCK:

                        child_Two_LL.addView(childView);

                        break;

                    case THIRD_BLOCK:

                        child_Three_LL.addView(childView);
                        break;

                    case FOURTH_BLOCK:

                        child_Four_LL.addView(childView);
                        break;
                }


                break;

            case TWO_BLOCK_HORIZONTAL:

                switch (viewtemplate_block) {

                    case FIRST_BLOCK:

                        child_One_LL.addView(childView);


                        break;

                    case SECOND_BLOCK:

                        child_Two_LL.addView(childView);
                        break;


                }

                break;
            case TWO_BLOCK_VERTICAL:

                switch (viewtemplate_block) {

                    case FIRST_BLOCK:

                        child_One_LL.addView(childView);


                        break;

                    case SECOND_BLOCK:

                        child_Two_LL.addView(childView);
                        break;


                }


                break;
            case SINGLE_BLOCK:

                switch (viewtemplate_block) {

                    case FIRST_BLOCK:

                        child_One_LL.addView(childView);


                        break;


                }
                break;
        }


    }


    public void restParentLL() {
        parentLL = new LinearLayout(context);

        LinearLayout.LayoutParams paramMain = new LinearLayout.LayoutParams(width, height);
//        paramMain.height=height;
//        paramMain.width=width;
        parentLL.setLayoutParams(paramMain);

        parentLL.setBackgroundColor(Color.parseColor("#ffffff"));
        parentLL.setOrientation(LinearLayout.VERTICAL);

    }


    public void finaliseViewForImage() {

        switch (viewtemplate) {

            case THREE_BLOCK:

                vertical_One_LL.addView(child_One_LL);
                vertical_Two_LL.addView(child_Two_LL);
                vertical_Two_LL.addView(child_Three_LL);
                // horizontal_One_LL.addView(vertical_One_LL);
                //  horizontal_One_LL.addView(vertical_Two_LL);
                parentLL.addView(vertical_One_LL);
                parentLL.addView(vertical_Two_LL);

                parentLL.setDrawingCacheEnabled(true);

                parentLL.measure(View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.EXACTLY),
                        View.MeasureSpec.makeMeasureSpec(height, View.MeasureSpec.EXACTLY));

                parentLL.layout(0, 0, width, height);

                parentLL.buildDrawingCache(true);
                break;

            case THREE_BLOCK_HEADER_ONE:

                vertical_One_LL.addView(child_One_LL);
                vertical_Two_LL.addView(child_Two_LL);
                vertical_Two_LL.addView(child_Three_LL);
                // horizontal_One_LL.addView(vertical_One_LL);
                //  horizontal_One_LL.addView(vertical_Two_LL);

                horizontal_One_LL.addView(vertical_One_LL);
                horizontal_One_LL.addView(vertical_Two_LL);

                parentLL.addView(header_one_LL);
                parentLL.addView(horizontal_One_LL);

                parentLL.setDrawingCacheEnabled(true);

                parentLL.measure(View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.EXACTLY),
                        View.MeasureSpec.makeMeasureSpec(height, View.MeasureSpec.EXACTLY));

                parentLL.layout(0, 0, width, height);

                parentLL.buildDrawingCache(true);
                break;

            case THREE_BLOCK_HEADER_TWO:

                vertical_One_LL.addView(child_One_LL);
                vertical_Two_LL.addView(child_Two_LL);
                vertical_Two_LL.addView(child_Three_LL);
                // horizontal_One_LL.addView(vertical_One_LL);
                //  horizontal_One_LL.addView(vertical_Two_LL);

                horizontal_One_LL.addView(vertical_One_LL);
                horizontal_One_LL.addView(vertical_Two_LL);

                parentLL.addView(header_one_LL);
                parentLL.addView(header_two_LL);
                parentLL.addView(horizontal_One_LL);

                parentLL.setDrawingCacheEnabled(true);

                parentLL.measure(View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.EXACTLY),
                        View.MeasureSpec.makeMeasureSpec(height, View.MeasureSpec.EXACTLY));

                parentLL.layout(0, 0, width, height);

                parentLL.buildDrawingCache(true);
                break;

            case FOUR_BLOCK:

                vertical_One_LL.addView(child_One_LL);
                vertical_One_LL.addView(child_Two_LL);
                vertical_Two_LL.addView(child_Three_LL);
                vertical_Two_LL.addView(child_Four_LL);
                // horizontal_One_LL.addView(vertical_One_LL);
                //  horizontal_One_LL.addView(vertical_Two_LL);
                parentLL.addView(vertical_One_LL);
                parentLL.addView(vertical_Two_LL);

                parentLL.setDrawingCacheEnabled(true);

                parentLL.measure(View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.EXACTLY),
                        View.MeasureSpec.makeMeasureSpec(height, View.MeasureSpec.EXACTLY));

                parentLL.layout(0, 0, width, height);

                parentLL.buildDrawingCache(true);
                break;

            case SINGLE_BLOCK:

                vertical_One_LL.addView(child_One_LL);
                parentLL.addView(vertical_One_LL);
                parentLL.setDrawingCacheEnabled(true);

                parentLL.measure(View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.EXACTLY),
                        View.MeasureSpec.makeMeasureSpec(height, View.MeasureSpec.EXACTLY));

                parentLL.layout(0, 0, width, height);

                parentLL.buildDrawingCache(true);
                break;

            case TWO_BLOCK_VERTICAL:

                vertical_One_LL.addView(child_One_LL);
                vertical_Two_LL.addView(child_Two_LL);
                parentLL.addView(vertical_One_LL);
                parentLL.addView(vertical_Two_LL);

                parentLL.setDrawingCacheEnabled(true);

                parentLL.measure(View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.EXACTLY),
                        View.MeasureSpec.makeMeasureSpec(height, View.MeasureSpec.EXACTLY));

                parentLL.layout(0, 0, width, height);

                parentLL.buildDrawingCache(true);

                break;

            case TWO_BLOCK_HORIZONTAL:

                vertical_One_LL.addView(child_One_LL);
                vertical_Two_LL.addView(child_Two_LL);
                parentLL.addView(vertical_One_LL);
                parentLL.addView(vertical_Two_LL);

                parentLL.setDrawingCacheEnabled(true);

                parentLL.measure(View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.EXACTLY),
                        View.MeasureSpec.makeMeasureSpec(height, View.MeasureSpec.EXACTLY));

                parentLL.layout(0, 0, width, height);

                parentLL.buildDrawingCache(true);

                break;
            case MISCELLINOUS:

                parentLL.addView(miscellinousView);

                parentLL.setDrawingCacheEnabled(true);

                parentLL.measure(View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.EXACTLY),
                        View.MeasureSpec.makeMeasureSpec(height, View.MeasureSpec.EXACTLY));

                parentLL.layout(0, 0, width, height);

                parentLL.buildDrawingCache(true);
                break;

          default:

                parentLL.addView(miscellinousView);

                parentLL.setDrawingCacheEnabled(true);

                parentLL.measure(View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.EXACTLY),
                        View.MeasureSpec.makeMeasureSpec(height, View.MeasureSpec.EXACTLY));

                parentLL.layout(0, 0, width, height);

                parentLL.buildDrawingCache(true);
                break;
        }

    }


    public Bitmap getFinalImage() {

//        ColorDrawable cd = new ColorDrawable(context.getResources().getColor(R.color.colorAccent));

//        Bitmap returnedBitmap = Bitmap.createBitmap(parentLL.getMeasuredWidth(), parentLL.getMeasuredHeight(),Bitmap.Config.ARGB_8888);
//        //Bind a canvas to it
//        Canvas canvas = new Canvas(returnedBitmap);
//        //Get the view's background
//        Drawable bgDrawable =parentLL.getBackground();
//        if (bgDrawable!=null) {
//            //has background drawable, then draw it on the canvas
//            bgDrawable.draw(canvas);
//        }   else{
//            //does not have background drawable, then draw white background on the canvas
//            canvas.drawColor(Color.WHITE);
//        }
//        // draw the view on the canvas
//        parentLL.draw(canvas);
//        //return the bitmap
//        return returnedBitmap;


        if (parentLL == null) {
            return BitmapFactory.decodeResource(context.getResources(), R.drawable.accountbalance);
        } else {
            Bitmap bitmap = Bitmap.createBitmap(1000, 1500, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);


            parentLL.draw(canvas);

         //   return Bitmap.createBitmap(parentLL.getDrawingCache());
            return bitmap;

        }


    }

    public Object getFinalDocument() {


        return parentLL;
    }


    public View getFinalView() {


        return parentLL;
    }

    public View createChildView(String stringType, ViewType viewType, int gravity, int height, int width, int size, int typeface, LayoutParams_LOCAL params, String text, Bitmap drawable) {
        View v = null;


        switch (viewType) {

            case TEXTVIEW:

                if (size == 0) {
                    size = 6;
                }
                textView = new TextView(context);
                textView.setLayoutParams(getParams(params, gravity, height, width, size));
                textView.setTypeface(textView.getTypeface(), typeface);
                textView.setTextColor(Color.parseColor("#000000"));
                textView.setTextSize(size);

                textView.setText(text);

                v = (View) textView;

                break;

            case IMAGEVIEW:
                ImageView aa =
                        new ImageView(context);
                aa.setLayoutParams(getParams(params, gravity, height, width, size));

                aa.setImageBitmap(drawable);
                v = (View) aa;
                break;

            case TABLEVIEW:

                /*TableLayout textLayout = new TableLayout(context);
                textLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));

                TableRow tableRow = new TableRow(context);
                tableRow.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));

                TextView commercialLabel = new TextView(context);
                commercialLabel.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                commercialLabel.setText("SIGNATURE");
                commercialLabel.setTextColor(Color.RED);
                commercialLabel.setTextSize(50);
                tableRow.addView(commercialLabel);

                v = (View) tableRow;*/

                break;
        }

        return v;
    }


    private LinearLayout.LayoutParams getParams(LayoutParams_LOCAL params, int gravity, int height, int width, int size) {
        LinearLayout.LayoutParams llP = null;
        switch (params) {

            case BOTH_MATCH_PARENT:

                llP = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                llP.gravity = gravity;
                break;


            case BOTH_WRAP_CONTENT:
                llP = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                llP.gravity = gravity;
                break;


            case WIDTH_MATCH_PARENT:

                llP = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);
                llP.gravity = gravity;
                break;

            case HEIGHT_MATCH_PARENT:
                llP = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                llP.gravity = gravity;

                break;

            case CUSTOM:
                llP = new LinearLayout.LayoutParams(height, width);
                llP.gravity = gravity;
                break;

            case SIZE:
                llP = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                llP.gravity = gravity;

                break;

            default:
                llP = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                llP.gravity = gravity;

                break;


        }


        return llP;

    }


}
