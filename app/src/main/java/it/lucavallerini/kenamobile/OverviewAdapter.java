package it.lucavallerini.kenamobile;

import android.content.Context;
import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

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

    public static class PromoCardView extends RecyclerView.ViewHolder {
        private PromoCardViewBinding binding;

        PromoCardView(View view, PromoCardViewBinding binding) {
            super(view);
            this.binding = binding;
            this.binding.setPromoInfo(this);
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
