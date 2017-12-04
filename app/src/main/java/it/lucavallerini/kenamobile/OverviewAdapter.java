package it.lucavallerini.kenamobile;

import android.content.Context;
import android.content.DialogInterface;
import android.databinding.BindingAdapter;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.ProgressBar;

import java.util.List;

import it.lucavallerini.kenamobile.databinding.CreditCardViewBinding;
import it.lucavallerini.kenamobile.databinding.PromoCardViewBinding;

public class OverviewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int CREDIT_CARD = 0;
    private static final int PROMO_CARD = 1;

    private List<Object> dashboardInfoList;

    OverviewAdapter(List<Object> dashboardInfoList) {
        this.dashboardInfoList = dashboardInfoList;
    }

    @BindingAdapter("fillProgressBar")
    public static void fillProgressBar(ProgressBar progressBar, int progress) {
        Animation animation = new ProgressBarAnimation(progressBar, 0, progress);
        animation.setDuration(1000);
        progressBar.setAnimation(animation);
    }

    @Override
    public int getItemCount() {
        return dashboardInfoList.size();
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder dashboardViewHolder, int i) {
        Object dashboardInfo = dashboardInfoList.get(i);

        if (dashboardViewHolder.getItemViewType() == CREDIT_CARD) {
            CreditCardViewBinding binding = ((CreditCardView) dashboardViewHolder).binding;
            binding.setVariable(BR.creditCard, dashboardInfo);
            binding.executePendingBindings();
        } else if (dashboardViewHolder.getItemViewType() == PROMO_CARD) {
            PromoCardViewBinding binding = ((PromoCardView) dashboardViewHolder).binding;
            binding.setVariable(BR.promoCard, dashboardInfo);
            binding.executePendingBindings();
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        LayoutInflater inflater =
                (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (viewType == CREDIT_CARD) {
            CreditCardViewBinding binding = DataBindingUtil.inflate(
                    inflater,
                    R.layout.credit_card_view,
                    viewGroup,
                    false
            );

            return new CreditCardView(binding.getRoot(), binding);

        } else {
            PromoCardViewBinding binding = DataBindingUtil.inflate(
                    inflater,
                    R.layout.promo_card_view,
                    viewGroup,
                    false
            );

            return new PromoCardView(binding.getRoot(), binding);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public static class CreditCardView extends RecyclerView.ViewHolder {
        private CreditCardViewBinding binding;

        CreditCardView(View view, CreditCardViewBinding binding) {
            super(view);
            this.binding = binding;
        }
    }

    /**
     * Progress bar animation class.
     */
    private static class ProgressBarAnimation extends Animation {
        private ProgressBar progressBar;
        private float from;
        private float to;

        ProgressBarAnimation(ProgressBar progressBar, float from, float to) {
            super();
            this.progressBar = progressBar;
            this.from = from;
            this.to = to;
        }

        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
            super.applyTransformation(interpolatedTime, t);
            float value = from + (to - from) * interpolatedTime;
            progressBar.setProgress((int) value);
        }

    }

    public class PromoCardView extends RecyclerView.ViewHolder {
        private PromoCardViewBinding binding;
//        private ProgressBar mCallsProgressBar;
//        private ProgressBar mDataProgressBar;
//        private ProgressBar mDataEuProgressBar;

        PromoCardView(View view, PromoCardViewBinding binding) {
            super(view);
            this.binding = binding;
            this.binding.setPromoInfo(this);

//            mCallsProgressBar = view.findViewById(R.id.callsProgressRemaining);
//            mDataProgressBar = view.findViewById(R.id.dataProgressRemaining);
//            mDataEuProgressBar = view.findViewById(R.id.dataEuProgressRemaining);
//
//            ProgressBarAnimation animationCalls =
//                    new ProgressBarAnimation(mCallsProgressBar, 0, 100);
//            animationCalls.setDuration(1000);
//            mCallsProgressBar.startAnimation(animationCalls);
//
//            ProgressBarAnimation  animationData =
//                    new ProgressBarAnimation(mDataProgressBar, 0, 100);
//            animationData.setDuration(1000);
//            mDataProgressBar.startAnimation(animationData);
//
//            ProgressBarAnimation  animationDataEu =
//                    new ProgressBarAnimation(mDataEuProgressBar, 0, 100);
//            animationDataEu.setDuration(1000);
//            mDataEuProgressBar.startAnimation(animationDataEu);
        }

        public void showDialog(Context context, String title, String message) {
            AlertDialog.Builder builder =
                    new AlertDialog.Builder(context)
                            .setTitle(title)
                            .setMessage(message)
                            .setPositiveButton(android.R.string.ok,
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    });

            builder.show();
        }
    }
}
