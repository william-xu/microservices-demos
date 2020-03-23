package org.xwl.demo.cloud.sentinel.util;

import org.springframework.cloud.alibaba.sentinel.rest.SentinelClientHttpResponse;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;

import com.alibaba.csp.sentinel.slots.block.BlockException;

public final class ExceptionUtil {

	private ExceptionUtil() {

	}

	public static SentinelClientHttpResponse handleException(HttpRequest request,
			byte[] body, ClientHttpRequestExecution execution, BlockException ex) {
		System.out.println("Oops: " + ex.getClass().getCanonicalName());
		return new SentinelClientHttpResponse("custom block info");
	}
	
    public static SentinelClientHttpResponse handleException(BlockException ex) {
        // Handler method that handles BlockException when blocked.
        // The method parameter list should match original method, with the last additional
        // parameter with type BlockException. The return type should be same as the original method.
        // The block handler method should be located in the same class with original method by default.
        // If you want to use method in other classes, you can set the blockHandlerClass
        // with corresponding Class (Note the method in other classes must be static).
        System.out.println("Oops: " + ex.getClass().getCanonicalName());
        return new SentinelClientHttpResponse("custom block info !!!!!");
    }
    
    public static String handleException(long s, BlockException ex) {
    	System.out.println(" blocking hello(long s) method. ");
        return "Oops: limit at the " + s + "invocation. blocked:: " + ex.getClass().getCanonicalName();
    }

    public static void handleExceptionWithoutReturn(BlockException ex) {
    	System.out.println(" blocking hello(long s) method. ");
    }
    
    
}