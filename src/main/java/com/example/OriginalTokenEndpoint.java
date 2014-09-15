package com.example;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import org.apache.oltu.oauth2.as.issuer.MD5Generator;
import org.apache.oltu.oauth2.as.issuer.OAuthIssuer;
import org.apache.oltu.oauth2.as.issuer.OAuthIssuerImpl;
import org.apache.oltu.oauth2.as.request.OAuthTokenRequest;
import org.apache.oltu.oauth2.as.response.OAuthASResponse;
import org.apache.oltu.oauth2.common.OAuth;
import org.apache.oltu.oauth2.common.error.OAuthError;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.OAuthResponse;
import org.apache.oltu.oauth2.common.message.types.GrantType;

@Path("/original_token")
public class OriginalTokenEndpoint {

	public static final String INVALID_CLIENT_DESCRIPTION = "Client authentication failed (e.g., unknown client, no client authentication included, or unsupported authentication method).";

	@POST
	@Consumes("application/x-www-form-urlencoded")
	@Produces("application/json")
	public Response authorize(@Context HttpServletRequest request) throws OAuthSystemException {
		System.out.println("################################");
		System.out.println("Token");

		OAuthTokenRequest oauthRequest = null;

		OAuthIssuer oauthIssuerImpl = new OAuthIssuerImpl(new MD5Generator());

		try {
			System.out.println("client id    : " + request.getParameter("client_id"));
			System.out.println("client secret: " + request.getParameter("client_secret")) ; 
			System.out.println("grant type   : " + request.getParameter("grant_type"));

			oauthRequest = new OAuthTokenRequest(request);
			

			// check if clientid is valid
			if (!Common.CLIENT_ID.equals(oauthRequest.getClientId())) {
				OAuthResponse response = OAuthASResponse.errorResponse(HttpServletResponse.SC_BAD_REQUEST).setError(OAuthError.TokenResponse.INVALID_CLIENT)
						.setErrorDescription(INVALID_CLIENT_DESCRIPTION).buildJSONMessage();
				return Response.status(response.getResponseStatus()).entity(response.getBody()).build();
			}

			// check if client_secret is valid
			if (!Common.CLIENT_SECRET.equals(oauthRequest.getClientSecret())) {
				OAuthResponse response = OAuthASResponse.errorResponse(HttpServletResponse.SC_UNAUTHORIZED)
						.setError(OAuthError.TokenResponse.UNAUTHORIZED_CLIENT).setErrorDescription(INVALID_CLIENT_DESCRIPTION).buildJSONMessage();
				return Response.status(response.getResponseStatus()).entity(response.getBody()).build();
			}

			// do checking for different grant types
			if (oauthRequest.getParam(OAuth.OAUTH_GRANT_TYPE).equals(GrantType.AUTHORIZATION_CODE.toString())) {
				if (!Common.AUTHORIZATION_CODE.equals(oauthRequest.getParam(OAuth.OAUTH_CODE))) {
					OAuthResponse response = OAuthASResponse.errorResponse(HttpServletResponse.SC_BAD_REQUEST).setError(OAuthError.TokenResponse.INVALID_GRANT)
							.setErrorDescription("invalid authorization code").buildJSONMessage();
					return Response.status(response.getResponseStatus()).entity(response.getBody()).build();
				}
			} else if (oauthRequest.getParam(OAuth.OAUTH_GRANT_TYPE).equals(GrantType.PASSWORD.toString())) {
				if (!Common.PASSWORD.equals(oauthRequest.getPassword()) || !Common.USERNAME.equals(oauthRequest.getUsername())) {
					OAuthResponse response = OAuthASResponse.errorResponse(HttpServletResponse.SC_BAD_REQUEST).setError(OAuthError.TokenResponse.INVALID_GRANT)
							.setErrorDescription("invalid username or password").buildJSONMessage();
					return Response.status(response.getResponseStatus()).entity(response.getBody()).build();
				}
			} else if (oauthRequest.getParam(OAuth.OAUTH_GRANT_TYPE).equals(GrantType.REFRESH_TOKEN.toString())) {
				// refresh token is not supported in this implementation
				OAuthResponse response = OAuthASResponse.errorResponse(HttpServletResponse.SC_BAD_REQUEST).setError(OAuthError.TokenResponse.INVALID_GRANT)
						.setErrorDescription("invalid username or password").buildJSONMessage();
				return Response.status(response.getResponseStatus()).entity(response.getBody()).build();
			}

			OAuthResponse response = OAuthASResponse.tokenResponse(HttpServletResponse.SC_OK).setAccessToken(oauthIssuerImpl.accessToken())
					.setExpiresIn("3600").buildJSONMessage();
			return Response.status(response.getResponseStatus()).entity(response.getBody()).build();

		} catch (OAuthProblemException e) {
			e.printStackTrace();
			OAuthResponse res = OAuthASResponse.errorResponse(HttpServletResponse.SC_BAD_REQUEST).error(e).buildJSONMessage();
			return Response.status(res.getResponseStatus()).entity(res.getBody()).build();
		}
	}
}