package com.greenapper.handlers;

import com.greenapper.forms.campaigns.CampaignForm;
import com.greenapper.forms.campaigns.OfferCampaignForm;
import org.springframework.core.MethodParameter;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.annotation.ModelFactory;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.ServletRequest;
import java.util.Map;

/**
 * Resolves method argument ambiguities when {@link CampaignForm} is used in method signatures at the controller level,
 * since this class cannot be instantiated, due to it being abstract. This resolver searches the generic campaign form
 * and extracts its type, and through reflection creates the appropriate campaign form that should be used in
 * each specific call.
 */
public class CampaignFormHandlerMethodArgumentResolver implements HandlerMethodArgumentResolver {

	@Override
	public boolean supportsParameter(final MethodParameter methodParameter) {
		return methodParameter.getParameterType().equals(CampaignForm.class);
	}

	@Override
	public Object resolveArgument(final MethodParameter methodParameter,
								  final ModelAndViewContainer modelAndViewContainer,
								  final NativeWebRequest nativeWebRequest,
								  final WebDataBinderFactory webDataBinderFactory) throws Exception {

		final String campaignType = nativeWebRequest.getParameter("type");

		final String name = ModelFactory.getNameForParameter(methodParameter);

		final CampaignForm campaignForm;
		if (campaignType != null) {
			final String campaignClassName = campaignType.charAt(0) + campaignType.substring(1).toLowerCase() + "CampaignForm";
			campaignForm = (CampaignForm) Class.forName(CampaignForm.class.getPackage().getName() + "." + campaignClassName).getConstructor().newInstance();
		} else
			campaignForm = new OfferCampaignForm();

		// Code borrowed from the default Spring method argument resolver, which is used to bind the form to an Errors instance for this request
		final WebDataBinder binder = webDataBinderFactory.createBinder(nativeWebRequest, campaignForm, name);
		bindParameters(nativeWebRequest, binder);
		final BindingResult bindingResult = binder.getBindingResult();

		final Map<String, Object> bindingResultModel = bindingResult.getModel();
		modelAndViewContainer.removeAttributes(bindingResultModel);
		modelAndViewContainer.addAllAttributes(bindingResultModel);
		return campaignForm;
	}

	// Copied from ServletModelAttributeMethodProcessor#bindRequestParameters(WebDataBinder, NativeWebRequest)
	private void bindParameters(final NativeWebRequest nativeWebRequest, final WebDataBinder binder) {
		ServletRequest servletRequest = nativeWebRequest.getNativeRequest(ServletRequest.class);
		Assert.state(servletRequest != null, "No ServletRequest");
		ServletRequestDataBinder servletBinder = (ServletRequestDataBinder) binder;
		servletBinder.bind(servletRequest);
	}
}