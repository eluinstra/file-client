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

import javax.xml.ws.Endpoint;
import javax.xml.ws.soap.SOAPBinding;

import org.apache.cxf.bus.spring.SpringBus;
import org.apache.cxf.endpoint.Server;
import org.apache.cxf.ext.logging.LoggingFeature;
import org.apache.cxf.jaxrs.JAXRSServerFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import dev.luin.digikoppeling.gb.client.service.GBService;
import dev.luin.digikoppeling.gb.client.service.GBServiceImpl;
import dev.luin.file.client.core.service.FileService;
import dev.luin.file.client.core.service.FileServiceImpl;
import io.vavr.Tuple;
import io.vavr.collection.HashMap;
import io.vavr.collection.Map;
import lombok.AccessLevel;
import lombok.val;
import lombok.experimental.FieldDefaults;

@Configuration
@FieldDefaults(level = AccessLevel.PRIVATE)
public class GBWebConfig extends WebConfig
{
	@Autowired
	FileService fileService;
	@Autowired
	GBService gbService;

	@Bean
	public Endpoint fileServiceEndpoint()
	{
		val result = publishEndpoint(fileService,"/file","http://luin.dev/file/client/1.0","FileService","FileServicePort");
		((SOAPBinding)result.getBinding()).setMTOMEnabled(true);
		return result;
	}

	@Bean
	public Endpoint gbServiceEndpoint()
	{
		return publishEndpoint(gbService,"/gb","http://luin.dev/digikoppeling/client/gb/1.0","GBService","GBServicePort");
	}

	@Bean(name="cxf")
	public SpringBus springBus()
	{
		val result = new SpringBus();
		val f = new LoggingFeature();
		f.setPrettyLogging(true);
		result.setFeatures(Collections.singletonList(f));
		return result;
	}

	@Bean
	public Server createJAXRSServer()
	{
		val sf = new JAXRSServerFactoryBean();
		sf.setBus(springBus());
		sf.setAddress("/rest/v1");
		sf.setProvider(createJacksonJsonProvider());
		registerResources(sf);
		registerBindingFactory(sf);
		return sf.create();
	}

	protected Map<Class<?>,Object> getResourceClasses()
	{
		val result = HashMap.<Class<?>,Object>ofEntries(
			Tuple.of(FileServiceImpl.class, fileService),
			Tuple.of(GBServiceImpl.class, gbService));
		return result;
	}

}
