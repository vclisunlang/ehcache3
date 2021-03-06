/*
 * Copyright Terracotta, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.ehcache.xml.service;

import org.ehcache.config.CacheConfiguration;
import org.ehcache.impl.config.store.heap.DefaultSizeOfEngineConfiguration;
import org.junit.Test;
import org.xml.sax.SAXException;

import java.io.IOException;

import javax.xml.bind.JAXBException;
import javax.xml.parsers.ParserConfigurationException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.ehcache.core.spi.service.ServiceUtils.findSingletonAmongst;

public class DefaultSizeOfEngineConfigurationParserTest extends ServiceConfigurationParserTestBase {

  public DefaultSizeOfEngineConfigurationParserTest() {
    super(new DefaultSizeOfEngineConfigurationParser());
  }

  @Test
  public void parseServiceConfiguration() throws ClassNotFoundException, SAXException, ParserConfigurationException, IOException, JAXBException {
    CacheConfiguration<?, ?> cacheConfig = getCacheDefinitionFrom("/configs/sizeof-engine.xml", "usesDefaultSizeOfEngine");
    DefaultSizeOfEngineConfiguration sizeOfEngineConfig = findSingletonAmongst(DefaultSizeOfEngineConfiguration.class, cacheConfig.getServiceConfigurations());

    assertThat(sizeOfEngineConfig).isNull();

    CacheConfiguration<?, ?> cacheConfig1 = getCacheDefinitionFrom("/configs/sizeof-engine.xml", "usesConfiguredInCache");
    DefaultSizeOfEngineConfiguration sizeOfEngineConfig1 = findSingletonAmongst(DefaultSizeOfEngineConfiguration.class, cacheConfig1.getServiceConfigurations());

    assertThat(sizeOfEngineConfig1).isNotNull();
    assertThat(sizeOfEngineConfig1.getMaxObjectGraphSize()).isEqualTo(500);
    assertThat(sizeOfEngineConfig1.getMaxObjectSize()).isEqualTo(200000);

    CacheConfiguration<?, ?> cacheConfig2 = getCacheDefinitionFrom("/configs/sizeof-engine.xml", "usesPartialOneConfiguredInCache");
    DefaultSizeOfEngineConfiguration sizeOfEngineConfig2 = findSingletonAmongst(DefaultSizeOfEngineConfiguration.class, cacheConfig2.getServiceConfigurations());

    assertThat(sizeOfEngineConfig2).isNotNull();
    assertThat(sizeOfEngineConfig2.getMaxObjectGraphSize()).isEqualTo(500L);
    assertThat(sizeOfEngineConfig2.getMaxObjectSize()).isEqualTo(Long.MAX_VALUE);

    CacheConfiguration<?, ?> cacheConfig3 = getCacheDefinitionFrom("/configs/sizeof-engine.xml", "usesPartialTwoConfiguredInCache");
    DefaultSizeOfEngineConfiguration sizeOfEngineConfig3 = findSingletonAmongst(DefaultSizeOfEngineConfiguration.class, cacheConfig3.getServiceConfigurations());

    assertThat(sizeOfEngineConfig3).isNotNull();
    assertThat(sizeOfEngineConfig3.getMaxObjectGraphSize()).isEqualTo(1000L);
    assertThat(sizeOfEngineConfig3.getMaxObjectSize()).isEqualTo(200000L);
  }
}
