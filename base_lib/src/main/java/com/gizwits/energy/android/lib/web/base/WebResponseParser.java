package com.gizwits.energy.android.lib.web.base;

/**
 * Parser
 *
 * @author Joke Huang
 * @version 1.0.0
 * @Description
 * @createDate 2014年7月9日
 */

public interface WebResponseParser<T> {
	T parse(WebResponse<T> webResponse) throws Exception;
}
