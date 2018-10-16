package com.mercadopago.android.px.internal.features.onetap;

import com.mercadopago.android.px.internal.repository.GroupsRepository;
import com.mercadopago.android.px.internal.repository.PaymentRepository;
import com.mercadopago.android.px.internal.repository.PaymentSettingRepository;
import com.mercadopago.android.px.internal.viewmodel.mappers.ElementDescriptorMapper;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@Ignore
@RunWith(MockitoJUnitRunner.class)
public class OneTapPresenterTest {

    @Mock
    private OneTap.View view;

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private PaymentSettingRepository configuration;

    @Mock
    private ElementDescriptorMapper elementDescriptorMapper;

    @Mock
    private GroupsRepository groupsRepository;

    private OneTapPresenter oneTapPresenter;

    @Before
    public void setUp() {
        oneTapPresenter = new OneTapPresenter(paymentRepository, configuration, elementDescriptorMapper,
            groupsRepository);
        oneTapPresenter.attachView(view);
    }

    @Test
    public void changePaymentMethod() {
        oneTapPresenter.changePaymentMethod();
        verify(view).changePaymentMethod();
        verifyNoMoreInteractions(view);
    }

    @Test
    public void onAmountShowMore() {
        oneTapPresenter.onAmountShowMore();
        verify(view).showDetailModal();
        verify(view).trackModal();
        verifyNoMoreInteractions(view);
    }

    @Test
    public void whenCanceledThenCancelAndTrack() {
        oneTapPresenter.cancel();
        verify(view).cancel();
        verify(view).trackCancel();
    }


    @Test
    public void whenPresenterDetachedThenPaymentRepositoryIsDetached(){
        verify(paymentRepository).attach(oneTapPresenter);
        oneTapPresenter.detachView();
        verify(paymentRepository).detach(oneTapPresenter);
        verifyNoMoreInteractions(paymentRepository);
    }

}