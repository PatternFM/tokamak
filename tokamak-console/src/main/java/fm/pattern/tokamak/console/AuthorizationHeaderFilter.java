package fm.pattern.tokamak.console;

import javax.servlet.http.HttpSession;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;

import fm.pattern.minimal.JSON;
import fm.pattern.tokamak.sdk.model.AccessTokenRepresentation;

public class AuthorizationHeaderFilter extends ZuulFilter {

	@Override
	public String filterType() {
		return "pre";
	}

	@Override
	public int filterOrder() {
		return 1;
	}

	@Override
	public boolean shouldFilter() {
		return true;
	}

	@Override
	public Object run() {
		RequestContext context = RequestContext.getCurrentContext();

		HttpSession session = RequestContext.getCurrentContext().getRequest().getSession(false);
		if (session == null) {
			return null;
		}

		AccessTokenRepresentation token = JSON.parse((String) session.getAttribute("token"), AccessTokenRepresentation.class);
		if (token != null) {
			context.addZuulRequestHeader("Authorization", "Bearer " + token.getAccessToken());
		}

		return null;
	}

}
