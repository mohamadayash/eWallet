package newwalletapp.views;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.example.accounts.newwalletapp.R;

import newwalletapp.adpter.CategoriesIconAdpter;

/**
 * Created by ahmedchoteri on 20-02-15.
 */
public class CustomIconDialog {
    public interface OnColorPickerListener {
        void onCancel(CustomIconDialog dialog);

        void onOk(CustomIconDialog dialog, int color,int icon);
    }

    final AlertDialog dialog;
    private final boolean supportsAlpha;
    final OnColorPickerListener listener;
    final View viewHue;
    final ColorPicker colorpicker;
    ImageView imageViewIcon;
    final ImageView viewCursor;
    final ImageView viewAlphaCursor;
    final View viewNewColor;
    //final View viewOldColor;
    final View viewAlphaOverlay;
    final ImageView viewTarget;
    final ImageView viewAlphaCheckered;
    final ViewGroup viewContainer;
    final GridView gridView;
    final float[] currentColorHsv = new float[3];
    int alpha;
    int selectedIcon;
    final Context context;

    public CustomIconDialog(final Context context, int color,int icon, boolean supportsAlpha, OnColorPickerListener listener) {
        this.supportsAlpha = supportsAlpha;
        this.listener = listener;
        this.context=context;
        this.selectedIcon=icon-1;
        if (!supportsAlpha) { // remove alpha if not supported
            color = color | 0xff000000;
        }

        Color.colorToHSV(color, currentColorHsv);
        alpha = Color.alpha(color);

        final View view = LayoutInflater.from(context).inflate(R.layout.dialog_custom_icon, null);
        imageViewIcon= (ImageView) view.findViewById(R.id.imageViewCategoriesIcon);
        viewHue = view.findViewById(R.id.colorPickerCustomCategoriesIconStrip);
        colorpicker = (ColorPicker) view.findViewById(R.id.colorPickerCustomCategoriesIcon);
        viewCursor = (ImageView) view.findViewById(R.id.colorPickerCustomCategoriesIconcursor);
        viewNewColor = view.findViewById(R.id.linearLayoutCustomCategoriesIconBackground);
        viewTarget = (ImageView) view.findViewById(R.id.colorPickerCustomCategoriesIcontarget);
        viewContainer = (ViewGroup) view.findViewById(R.id.colorPickerCustomCategoriesIconContainer);
        viewAlphaOverlay = view.findViewById(R.id.colorPickerCustomCategoriesIconOverlyView);
        viewAlphaCheckered = (ImageView) view.findViewById(R.id.colorPickerCustomCategoriesIconalphaCheckered);
        viewAlphaCursor = (ImageView) view.findViewById(R.id.colorPickerCustomCategoriesIconalphaCursor);
        viewAlphaOverlay.setVisibility(supportsAlpha ? View.VISIBLE : View.GONE);
        viewAlphaCursor.setVisibility(supportsAlpha ? View.VISIBLE : View.GONE);
        viewAlphaCheckered.setVisibility(supportsAlpha ? View.VISIBLE : View.GONE);
        colorpicker.setHue(getHue());
        viewNewColor.setBackgroundColor(color);

        gridView= (GridView) view.findViewById(R.id.gridViewCategoryIcon);


        final Drawable[] drawablesBlack=new Drawable[59];
        final Drawable[] drawablesWhite=new Drawable[59];
        for(int i=0;i<59;i++)
        {
            //icon[i]=i+1;
            Log.d("Check I","i="+i);
             drawablesBlack[i] = context.getResources().getDrawable(context.getResources()
                    .getIdentifier("categoury_b_"+(i+1), "drawable", context.getApplicationContext().getPackageName()));
            drawablesWhite[i] = context.getResources().getDrawable(context.getResources()
                    .getIdentifier("categoury_w_"+(i+1), "drawable", context.getApplicationContext().getPackageName()));

        }

        imageViewIcon.setImageDrawable(drawablesWhite[selectedIcon]);
        CategoriesIconAdpter categoriesIconAdpter=new CategoriesIconAdpter(context,drawablesBlack);
        gridView.setAdapter(categoriesIconAdpter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                  imageViewIcon.setImageDrawable(drawablesWhite[position]);
                  selectedIcon=position;
            }
        });






        viewHue.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_MOVE
                        || event.getAction() == MotionEvent.ACTION_DOWN
                        || event.getAction() == MotionEvent.ACTION_UP) {

                    float y = event.getY();
                    if (y < 0.f) y = 0.f;
                    if (y > viewHue.getMeasuredHeight()) {
                        y = viewHue.getMeasuredHeight() - 0.001f; // to avoid jumping the cursor from bottom to top.
                    }
                    float hue = 360.f - 360.f / viewHue.getMeasuredHeight() * y;
                    if (hue == 360.f) hue = 0.f;
                    setHue(hue);

                    // update view
                    colorpicker.setHue(getHue());
                    moveCursor();
                    viewNewColor.setBackgroundColor(getColor());


                    updateAlphaView();
                    return true;
                }
                return false;
            }
        });

        if (supportsAlpha) viewAlphaCheckered.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if ((event.getAction() == MotionEvent.ACTION_MOVE)
                        || (event.getAction() == MotionEvent.ACTION_DOWN)
                        || (event.getAction() == MotionEvent.ACTION_UP)) {

                    float y = event.getY();
                    if (y < 0.f) {
                        y = 0.f;
                    }
                    if (y > viewAlphaCheckered.getMeasuredHeight()) {
                        y = viewAlphaCheckered.getMeasuredHeight() - 0.001f; // to avoid jumping the cursor from bottom to top.
                    }
                    final int a = Math.round(255.f - ((255.f / viewAlphaCheckered.getMeasuredHeight()) * y));
                    CustomIconDialog.this.setAlpha(a);

                    // update view
                    moveAlphaCursor();
                    int col = CustomIconDialog.this.getColor();
                    int c = a << 24 | col & 0x00ffffff;
                    viewNewColor.setBackgroundColor(c);


                    return true;
                }
                return false;
            }
        });
        colorpicker.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_MOVE
                        || event.getAction() == MotionEvent.ACTION_DOWN
                        || event.getAction() == MotionEvent.ACTION_UP) {

                    float x = event.getX(); // touch event are in dp units.
                    float y = event.getY();

                    if (x < 0.f) x = 0.f;
                    if (x > colorpicker.getMeasuredWidth()) x = colorpicker.getMeasuredWidth();
                    if (y < 0.f) y = 0.f;
                    if (y > colorpicker.getMeasuredHeight()) y = colorpicker.getMeasuredHeight();

                    setSat(1.f / colorpicker.getMeasuredWidth() * x);
                    setVal(1.f - (1.f / colorpicker.getMeasuredHeight() * y));

                    // update view
                    moveTarget();
                    viewNewColor.setBackgroundColor(getColor());


                    return true;
                }
                return false;
            }
        });

        dialog = new AlertDialog.Builder(context)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (CustomIconDialog.this.listener != null) {
                            CustomIconDialog.this.listener.onOk(CustomIconDialog.this, getColor(),selectedIcon);
                        }
                    }
                })
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (CustomIconDialog.this.listener != null) {
                            CustomIconDialog.this.listener.onCancel(CustomIconDialog.this);
                        }
                    }
                })
                .setOnCancelListener(new DialogInterface.OnCancelListener() {
                    // if back button is used, call back our listener.
                    @Override
                    public void onCancel(DialogInterface paramDialogInterface) {
                        if (CustomIconDialog.this.listener != null) {
                            CustomIconDialog.this.listener.onCancel(CustomIconDialog.this);
                        }

                    }
                })
                .create();
        // kill all padding from the dialog window
        //dialog.setContentView(view);

        dialog.setView(view);


        Log.d("Dialog Check", "dialog.setView(view, 0, 0, 0, 0);");
        // move cursor & target on first draw
        ViewTreeObserver vto = view.getViewTreeObserver();
        Log.d("Dialog Check", "ViewTreeObserver vto = view.getViewTreeObserver();");
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                moveCursor();
                if (CustomIconDialog.this.supportsAlpha) moveAlphaCursor();
                moveTarget();
                if (CustomIconDialog.this.supportsAlpha) updateAlphaView();
                view.getViewTreeObserver().removeGlobalOnLayoutListener(this);
            }
        });
    }


    protected void moveCursor() {
        float y = viewHue.getMeasuredHeight() - (getHue() * viewHue.getMeasuredHeight() / 360.f);
        if (y == viewHue.getMeasuredHeight()) y = 0.f;
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) viewCursor.getLayoutParams();
        layoutParams.leftMargin = (int) (viewHue.getLeft() - Math.floor(viewCursor.getMeasuredWidth() / 2) - viewContainer.getPaddingLeft());
        layoutParams.topMargin = (int) (viewHue.getTop() + y - Math.floor(viewCursor.getMeasuredHeight() / 2) - viewContainer.getPaddingTop());
        viewCursor.setLayoutParams(layoutParams);
    }

    protected void moveTarget() {
        float x = getSat() * colorpicker.getMeasuredWidth();
        float y = (1.f - getVal()) * colorpicker.getMeasuredHeight();
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) viewTarget.getLayoutParams();
        layoutParams.leftMargin = (int) (colorpicker.getLeft() + x - Math.floor(viewTarget.getMeasuredWidth() / 2) - viewContainer.getPaddingLeft());
        layoutParams.topMargin = (int) (colorpicker.getTop() + y - Math.floor(viewTarget.getMeasuredHeight() / 2) - viewContainer.getPaddingTop());
        viewTarget.setLayoutParams(layoutParams);
    }

    protected void moveAlphaCursor() {
        final int measuredHeight = this.viewAlphaCheckered.getMeasuredHeight();
        float y = measuredHeight - ((this.getAlpha() * measuredHeight) / 255.f);
        final RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) this.viewAlphaCursor.getLayoutParams();
        layoutParams.leftMargin = (int) (this.viewAlphaCheckered.getLeft() - Math.floor(this.viewAlphaCursor.getMeasuredWidth() / 2) - this.viewContainer.getPaddingLeft());
        layoutParams.topMargin = (int) ((this.viewAlphaCheckered.getTop() + y) - Math.floor(this.viewAlphaCursor.getMeasuredHeight() / 2) - this.viewContainer.getPaddingTop());

        this.viewAlphaCursor.setLayoutParams(layoutParams);
    }

    private int getColor() {
        final int argb = Color.HSVToColor(currentColorHsv);
        return alpha << 24 | (argb & 0x00ffffff);
    }

    private float getHue() {
        return currentColorHsv[0];
    }

    private float getAlpha() {
        return this.alpha;
    }

    private float getSat() {
        return currentColorHsv[1];
    }

    private float getVal() {
        return currentColorHsv[2];
    }

    private void setHue(float hue) {
        currentColorHsv[0] = hue;
    }

    private void setSat(float sat) {
        currentColorHsv[1] = sat;
    }

    private void setAlpha(int alpha) {
        this.alpha = alpha;
    }

    private void setVal(float val) {
        currentColorHsv[2] = val;
    }

    public void show() {
        dialog.show();
        Window window = dialog.getWindow();

        window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        Drawable[] drawables=new Drawable[59];
        for(int i=0;i<59;i++)
        {
            //icon[i]=i+1;
            Log.d("Check I","i="+i);
            drawables[i] = context.getResources().getDrawable(context.getResources()
                    .getIdentifier("categoury_b_"+(i+1), "drawable", context.getApplicationContext().getPackageName()));

        }
        CategoriesIconAdpter categoriesIconAdpter=new CategoriesIconAdpter(context,drawables);
        gridView.setAdapter(categoriesIconAdpter);

    }

    public AlertDialog getDialog() {
        return dialog;
    }

    private void updateAlphaView() {
        final GradientDrawable gd = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, new int[]{
                Color.HSVToColor(currentColorHsv), 0x0
        });
        viewAlphaOverlay.setBackgroundDrawable(gd);
    }


}
