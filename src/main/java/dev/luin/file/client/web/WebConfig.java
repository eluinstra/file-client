/**
 * Copyright 2020 E.Luinstra
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package dev.luin.file.client.web;

import java.util.Collections;

import javax.xml.namespace.QName;
import javax.xml.ws.Endpoint;
import javax.xml.ws.soap.SOAPBinding;

import org.apache.cxf.bus.spring.SpringBus;
import org.apache.cxf.ext.logging.LoggingFeature;
import org.apache.cxf.jaxws.EndpointImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import dev.luin.file.client.core.service.FileService;
import lombok.AccessLevel;
import lombok.val;
import lombok.experimental.FieldDefaults;

@Configuration
@FieldDefaults(level = AccessLevel.PRIVATE)
public class WebConfig
{
	@Autowired
	FileService fileService;

	@Bean
	public Endpoint fileServiceEndpoint()
	{
		val result = publishEndpoint(fileService,"/file","http://luin.dev/file/client/1.0","FileService","FileServicePort");
		((SOAPBinding)result.getBinding()).setMTOMEnabled(true);
		return result;
	}

	@Bean
	public SpringBus cxf()
	{
		val result = new SpringBus();
		val f = new LoggingFeature();
		f.setPrettyLogging(true);
		result.setFeatures(Collections.singletonList(f));
		return result;
	}

	private Endpoint publishEndpoint(Object service, String address, String namespaceUri, String serviceName, String endpointName)
	{
		val result = new EndpointImpl(cxf(),service);
		result.setAddress(address);
		result.setServiceName(new QName(namespaceUri,serviceName));
		result.setEndpointName(new QName(namespaceUri,endpointName));
		result.publish();
		return result;
	}
}
