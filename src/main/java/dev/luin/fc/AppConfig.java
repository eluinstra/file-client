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
package dev.luin.fc;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

import dev.luin.fc.core.datasource.DataSourceConfig;
import dev.luin.fc.core.download.DownloadClientConfig;
import dev.luin.fc.core.file.FileSystemConfig;
import dev.luin.fc.core.querydsl.QueryDSLConfig;
import dev.luin.fc.core.service.ServiceConfig;
import dev.luin.fc.core.transaction.TransactionManagerConfig;
import dev.luin.fc.core.upload.UploadClientConfig;
import dev.luin.fc.web.WebConfig;
import lombok.AccessLevel;
import lombok.val;
import lombok.experimental.FieldDefaults;

@Configuration
@Import({
	DataSourceConfig.class,
	DownloadClientConfig.class,
	FileSystemConfig.class,
	QueryDSLConfig.class,
	ServiceConfig.class,
	TransactionManagerConfig.class,
	UploadClientConfig.class,
	WebConfig.class
})
@PropertySource(value = {
		"classpath:dev/luin/fc/core/default.properties",
		"classpath:dev/luin/fc/default.properties",
		"file:${fc.configDir}file-client.advanced.properties",
		"file:${fc.configDir}file-client.properties"},
		ignoreResourceNotFound = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AppConfig
{
	public static PropertySourcesPlaceholderConfigurer PROPERTY_SOURCE = propertySourcesPlaceholderConfigurer();
	
	private static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer()
	{
		val result = new PropertySourcesPlaceholderConfigurer();
		val configDir = System.getProperty("fc.configDir");
		val resources = new Resource[]{
				new ClassPathResource("dev/luin/fc/core/default.properties"),
				new ClassPathResource("dev/luin/fc/default.properties"),
				new FileSystemResource(configDir + "file-client.advanced.properties"),
				new FileSystemResource(configDir + "file-client.properties")};
		result.setLocations(resources);
		result.setIgnoreResourceNotFound(true);
		return result;
	}
}
