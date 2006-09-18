/*
 * Copyright 2005-2006 Noelios Consulting.
 *
 * The contents of this file are subject to the terms
 * of the Common Development and Distribution License
 * (the "License").  You may not use this file except
 * in compliance with the License.
 *
 * You can obtain a copy of the license at
 * http://www.opensource.org/licenses/cddl1.txt
 * See the License for the specific language governing
 * permissions and limitations under the License.
 *
 * When distributing Covered Code, include this CDDL
 * HEADER in each file and include the License file at
 * http://www.opensource.org/licenses/cddl1.txt
 * If applicable, add the following below this CDDL
 * HEADER, with the fields enclosed by brackets "[]"
 * replaced with your own identifying information:
 * Portions Copyright [yyyy] [name of copyright owner]
 */
package com.noelios.restlet.ext.asyncweb;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;

import org.restlet.data.Parameter;
import org.restlet.data.ParameterList;
import org.restlet.data.Reference;
import org.restlet.data.Representation;
import org.safehaus.asyncweb.http.HttpRequest;
import org.safehaus.asyncweb.http.HttpResponse;
import org.safehaus.asyncweb.http.ResponseStatus;
import org.safehaus.asyncweb.http.internal.HttpHeaders;
import org.safehaus.asyncweb.http.internal.Request;
import org.safehaus.asyncweb.http.internal.Response;

import com.noelios.restlet.impl.connector.HttpServerCall;

/**
 * HttpServerCall implementation used by the AsyncServer.
 * @author Lars Heuer (heuer[at]semagia.com) <a href="http://www.semagia.com/">Semagia</a>
 */
public class AsyncWebServerCall extends HttpServerCall
{
	/**
	 * AsyncWeb request.
	 */
	private Request request;

	/** Indicates if the request headers were parsed and added. */
	private boolean requestHeadersAdded;

	/**
	 * AsyncWeb response.
	 */
	private Response response;

	/**
	 * Constructor.
	 *
	 * @param request The AsyncWebRequest.
	 * @param response The AsyncWebResponse.
	 * @param confidential Indicates if the server is acting in HTTPS mode.
	 * @param address IP address of the server.
	 */
	public AsyncWebServerCall(HttpRequest request, HttpResponse response,
			boolean confidential, String address)
	{
		super();
		this.request = (Request) request;
		this.requestHeadersAdded = false;
		this.response = (Response) response;
		setConfidential(confidential);
		setResponseAddress(address);
	}

	@Override
	public String getRequestAddress()
	{
		return request.getRemoteAddress();
	}

	@Override
	public String getRequestUri()
	{
		return Reference.toString(isConfidential() ? "https" : "http", request
				.getHeader("host"), null, request.getRequestURI(), null, null);
	}

	@Override
	public String getRequestMethod()
	{
		return request.getMethod().getName();
	}

	@Override
	public ParameterList getRequestHeaders()
	{
		ParameterList result = super.getRequestHeaders();

		if(!this.requestHeadersAdded)
		{
			HttpHeaders headers = request.getHeaders();
			int headerCount = headers.getSize();
			for (int i = 0; i < headerCount; i++)
			{
				result.add(headers.getHeaderName(i).getValue(), headers.getHeaderValue(i)
						.getValue());
			}

			this.requestHeadersAdded = true;
		}

		return result;
	}

	/**
	 * Sends the response back to the client. Commits the status, headers and optional output and 
	 * send them on the network. 
	 * @param output The optional output representation to send.
	 */
	public void sendResponse(Representation output) throws IOException
	{
		response.setStatus(ResponseStatus.forId(getResponseStatusCode()),
				getResponseReasonPhrase());

		// Ensure that headers are empty
		response.getHeaders().dispose();
		for (Parameter header : super.getResponseHeaders())
		{
			response.addHeader(header.getName(), header.getValue());
		}

		// Send the output representation
		super.sendResponse(output);
	}

	@Override
	public ReadableByteChannel getRequestChannel()
	{
		// Unsupported.
		return null;
	}

	@Override
	public InputStream getRequestStream()
	{
		return request.getInputStream();
	}

	@Override
	public WritableByteChannel getResponseChannel()
	{
		// Unsupported.
		return null;
	}

	@Override
	public OutputStream getResponseStream()
	{
		return response.getOutputStream();
	}

}
