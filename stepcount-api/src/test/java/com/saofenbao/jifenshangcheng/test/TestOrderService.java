package com.saofenbao.jifenshangcheng.test;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

import com.saofenbao.jifenshangcheng.dao.OrderDao;
import com.saofenbao.jifenshangcheng.dao.VendorGiftDao;
import com.saofenbao.jifenshangcheng.domain.ConsigneeAddress;
import com.saofenbao.jifenshangcheng.domain.Order;
import com.saofenbao.jifenshangcheng.domain.OrderItem;
import com.saofenbao.jifenshangcheng.domain.VendorGift;
import com.saofenbao.jifenshangcheng.service.OrderService;


@RunWith(MockitoJUnitRunner.class)
public class TestOrderService {
	private static final Log log = LogFactory.getLog(TestOrderService.class);
	
    @Mock
    private OrderDao orderDao;
    @Mock
    private VendorGiftDao giftDao;
    
    @InjectMocks
    private OrderService orderService = new OrderService();

    private Order orderToSave, persistedOrder;
    private final int ORDERID = 12345;
    private final int VENDORID = 99;
    
    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        
    	ConsigneeAddress cAddr = new ConsigneeAddress();
    	cAddr.setConsignee("zhangsan");
    	
    	List<OrderItem> list = new ArrayList<OrderItem>();
    	OrderItem item1 = new OrderItem();
    	item1.setGiftId("GIFT001");
    	item1.setNum(2);
    	list.add(item1);
    	OrderItem item2 = new OrderItem();
    	item2.setGiftId("GIFT002");
    	item2.setNum(2);
    	list.add(item2);
    	
    	orderToSave = new Order();
    	orderToSave.setVendorId(VENDORID);
    	orderToSave.setConsigneeAddress(cAddr);
    	orderToSave.setOrderItems(list);
    	
        // Expected objects
    	Order persistedOrder = new Order();
        
        persistedOrder.setId(ORDERID);
        persistedOrder.setVendorId(VENDORID);
        persistedOrder.setConsigneeAddress(cAddr);
        persistedOrder.setOrderItems(list);
    }
  
    @Test
    public void retrieveOrder(){
    	when(orderDao.getOrderById(ORDERID)).thenReturn(persistedOrder);
    	//when(orderDao.getOrderById(1234)).thenReturn(null);
    	Order o1 = orderService.retrieveOrder(ORDERID);
    	assertEquals(o1, persistedOrder);
    	Order o2 = orderService.retrieveOrder(987);
    	assertNull(o2);
    }
    
    @Test
    public void createNewOrder() {
//    	Order order = Mockito.mock(Order.class);
        doAnswer(new Answer<Object>() {

			@Override
			public Object answer(InvocationOnMock invocation) throws Throwable {
				Object[] args = invocation.getArguments();
                ((Order) args[0]).setId(ORDERID);
				return 1;
			}
        }).when(orderDao).insertOrder(any(Order.class));
        
        final VendorGift gift001 = new VendorGift(); // Mockito.mock(VendorGift.class);
        gift001.setJifen(111);
        gift001.setSupplyPrice(11.11);
        final VendorGift gift002 = new VendorGift(); //Mockito.mock(VendorGift.class);
        gift002.setJifen(222);
        gift002.setSupplyPrice(22.22);
        when(giftDao.getVendorGift(VENDORID, "GIFT001")).thenReturn(gift001);
        when(giftDao.getVendorGift(VENDORID, "GIFT002")).thenReturn(gift002);
        /*
         * 下面这段代码和上面的等效；这里只是记录另外一种写法
         * 
        doAnswer(new Answer<Object>() {

			@Override
			public Object answer(InvocationOnMock invocation) throws Throwable {
				log.trace("answer for giftDao.getVendorGift...");
				Object[] args = invocation.getArguments();
                if(((String) args[1]).equals("GIFT001")){
                	return gift001;
                }else if(((String) args[1]).equals("GIFT002")){
                	return gift002;
                }
				fail("不该到这里，测试用例异常");
				return null;
			}
        }).when(giftDao).getVendorGift(any(int.class), any(String.class));
        */

        when(giftDao.getVendorGift(VENDORID, "GIFT002")).thenReturn(gift002);
        
        Order newOrder = orderService.create(orderToSave);

        assertNotNull(newOrder);
        assertEquals(ORDERID, newOrder.getId());
        assertEquals(orderToSave.getConsigneeAddress().getConsignee(), newOrder.getConsigneeAddress().getConsignee());

        for(OrderItem item : orderToSave.getOrderItems()){
        	log.trace("ITEM:" + item.toString());
        }
        assertTrue(newOrder.getOrderItems().get(0).getSupplyPrice() == 11.11);
        assertTrue(newOrder.getOrderItems().get(0).getJifen() == 111);
        assertTrue(newOrder.getOrderItems().get(1).getSupplyPrice() == 22.22);
        assertTrue(newOrder.getOrderItems().get(1).getJifen() == 222);
        //verify(notificationService).notifyOfNewAccount(accountId);
        verify(orderDao).insertOrder(orderToSave);
  
    }
}
