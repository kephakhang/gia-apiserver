package kr.co.korbit.gia.yml

import org.springframework.core.env.PropertiesPropertySource
import org.springframework.core.io.support.EncodedResource

class YamlResourcePropertySource(name: String?, resource: EncodedResource) : PropertiesPropertySource(name!!, YamlPropertiesProcessor(resource.resource).createProperties()) 