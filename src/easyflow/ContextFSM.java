package easyflow;



import static au.com.ds.ef.FlowBuilder.from;
import static au.com.ds.ef.FlowBuilder.on;
import static easyflow.ContextFSM.Events.*;
import static easyflow.ContextFSM.States.*;


import easyflow.ContextFSM.FlowContext;

import au.com.ds.ef.*;
import au.com.ds.ef.call.ContextHandler;


public class ContextFSM {
	public static class FlowContext extends StatefulContext {
		public int balance = 1000;
		public String cardId = "123456789";
    }
	
	 public enum States implements StateEnum {
		 START,
		 CARD_INVALID,
		 BALACE_CHECK,
		 STARTTRANSACTION,
		 EXIT
	 }
	 
	 public enum Events implements EventEnum {
		 	validcard,
		 	transaction,
	    	startTransaction,
	    	notvalid,
	    	stop
	 }
	 
	 private EasyFlow<FlowContext> easyFlow;

	 public static void main(String[] args) throws InterruptedException {
		 ContextFSM m = new ContextFSM();

	        m.initFlow();
	        m.bindFlow();
	        m.startFlow();
    }
	    
	private void initFlow() {
		 easyFlow =
				from(START).transit(
					on(validcard).to(BALACE_CHECK).transit(
                    		on(startTransaction).to(STARTTRANSACTION).transit (
                    			on(stop).finish(EXIT)
                    		),
                    		on(stop).finish(EXIT)
						 ), 					 
		    		on(notvalid).to(CARD_INVALID).transit(
						on(stop).finish(EXIT)
					 ),
					on(stop).finish(EXIT)
				);
    }
	
	private void bindFlow() {
		easyFlow

        .whenEnter(START, new ContextHandler<FlowContext>() {
		
        public void call(final FlowContext context) throws Exception {
        	System.out.println("START State Entered \n");
        	System.out.println("Checking wheather the card is valid or not \n");
        	if(CardOps.isCardValid()){
        		System.out.println("Valid Card \n");
        		context.trigger(validcard);
        	} else	{
        		System.out.println("InValid Card \n");
        		context.trigger(notvalid);
        	}
        	System.out.println("START State End \n");
        	}
        })
        
        .whenEnter(BALACE_CHECK, new ContextHandler<FlowContext>() {
		
        public void call(final FlowContext context) throws Exception {
    		System.out.println("BALACE_CHECK state Entered \n");
        	if(CardOps.balanceCheck()){
        		System.out.println("BALANCE OK \t" + CardOps.balance );
        		context.trigger(startTransaction);
        	} else	{
        		System.out.println("IN-SUFFICIENT BALANCE\n");
        		context.trigger(stop);
        	}
        	System.out.println("\nBALACE_CHECK state End \n");
        	}
        })
        
        .whenEnter(STARTTRANSACTION, new ContextHandler<FlowContext>() {
		
        public void call(final FlowContext context) throws Exception {
        		System.out.println("START TRANSACTION state Entered \n");
        		System.out.println("START TRANSCATION \t Balance" + "\t" +CardOps.balance);
        		System.out.println("PAYING AMOUNT \t\t\t\t" +CardOps.payAmount);
        		CardOps.transaction();
        		System.out.println("After TRANSCATION \t Balance" + "\t" +CardOps.balance);
        		context.trigger(startTransaction);
        		System.out.println("\nSTART TRANSACTION state End \n");
        	}
        })
		
		.whenEnter(CARD_INVALID, new ContextHandler<FlowContext>() {
			
	        public void call(final FlowContext context) throws Exception {
	        		System.out.println("CARD_INVALID State Entered \n");
	        		System.out.println("CARD is Invalid \n");
	        		context.trigger(stop);
	        		System.out.println("CARD_INVALID State End \n");
	        	}
	        })
	        
		  .whenEnter(EXIT, new ContextHandler<FlowContext>() {
              
              public void call(FlowContext flowContext) throws Exception {
              	System.out.println("EXIT \n");
                  System.exit(0);
              }
          });
	}
	
	private void startFlow() {
		easyFlow.start(new FlowContext());
	}

}

