package json.chao.com.wanandroid.presenter.main;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import json.chao.com.wanandroid.core.DataManager;
import json.chao.com.wanandroid.base.presenter.BasePresenter;
import json.chao.com.wanandroid.contract.main.SearchContract;
import json.chao.com.wanandroid.core.bean.BaseResponse;
import json.chao.com.wanandroid.core.bean.main.search.TopSearchData;
import json.chao.com.wanandroid.core.bean.main.search.UsefulSiteData;
import json.chao.com.wanandroid.core.dao.HistoryData;
import json.chao.com.wanandroid.utils.RxUtils;
import json.chao.com.wanandroid.widget.BaseObserver;

/**
 * @author quchao
 * @date 2018/2/12
 */

public class SearchPresenter extends BasePresenter<SearchContract.View> implements SearchContract.Presenter {

    private DataManager mDataManager;

    @Inject
    SearchPresenter(DataManager dataManager) {
        super(dataManager);
        this.mDataManager = dataManager;
    }

    @Override
    public void attachView(SearchContract.View view) {
        super.attachView(view);
    }

    @Override
    public void addHistoryData(String data) {
        addSubscribe(Observable.create((ObservableOnSubscribe<List<HistoryData>>) e -> {
            List<HistoryData> historyDataList = mDataManager.addHistoryData(data);
            e.onNext(historyDataList);
        }).compose(RxUtils.rxSchedulerHelper()).subscribe(historyDataList ->
                mView.judgeToTheSearchListActivity()));
    }

    @Override
    public void clearHistoryData() {
        mDataManager.clearHistoryData();
    }

    @Override
    public void getTopSearchData() {
        addSubscribe(mDataManager.getTopSearchData()
                        .compose(RxUtils.rxSchedulerHelper())
                        .subscribeWith(new BaseObserver<BaseResponse<List<TopSearchData>>>(mView) {
                            @Override
                            public void onNext(BaseResponse<List<TopSearchData>> topSearchDataResponse) {
                                if (topSearchDataResponse.getErrorCode() == BaseResponse.SUCCESS) {
                                    mView.showTopSearchData(topSearchDataResponse);
                                } else {
                                    mView.showTopSearchDataFail();
                                }
                            }
                        }));
    }

}
