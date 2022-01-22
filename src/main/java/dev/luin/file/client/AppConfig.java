/*
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
package dev.luin.file.client;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

import dev.luin.file.client.core.datasource.DataSourceConfig;
import dev.luin.file.client.core.download.DownloadClientConfig;
import dev.luin.file.client.core.file.FileSystemConfig;
import dev.luin.file.client.core.querydsl.QueryDSLConfig;
import dev.luin.file.client.core.security.KeyStoreConfig;
import dev.luin.file.client.core.service.ServiceConfig;
import dev.luin.file.client.core.transaction.TransactionManagerConfig;
import dev.luin.file.client.core.upload.UploadClientConfig;
import dev.luin.file.client.web.WebConfig;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.val;
import lombok.experimental.FieldDefaults;

@Configuration
@Import({
	DataSourceConfig.class,
	DownloadClientConfig.class,
	FileSystemConfig.class,
	KeyStoreConfig.class,
	QueryDSLConfig.class,
	ServiceConfig.class,
	TransactionManagerConfig.class,
	UploadClientConfig.class,
	WebConfig.class
})
@PropertySource(value = {
		"classpath:dev/luin/file/client/core/default.properties",
		"classpath:dev/luin/file/client/default.properties",
		"file:${configDir}file-client.advanced.properties",
		"file:${configDir}file-client.properties"},
		ignoreResourceNotFound = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class AppConfig
{
	public static final PropertySourcesPlaceholderConfigurer PROPERTY_SOURCE = propertySourcesPlaceholderConfigurer();
	
	private static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer()
	{
		val result = new PropertySourcesPlaceholderConfigurer();
		val configDir = System.getProperty("configDir");
		val resources = new Resource[]{
				new ClassPathResource("dev/luin/file/client/core/default.properties"),
				new ClassPathResource("dev/luin/file/client/default.properties"),
				new FileSystemResource(configDir + "file-client.advanced.properties"),
				new FileSystemResource(configDir + "file-client.properties")};
		result.setLocations(resources);
		result.setIgnoreResourceNotFound(true);
		return result;
	}
}
