package world.estaria.kube.configmap.kit

import com.charleskorn.kaml.Yaml
import io.fabric8.kubernetes.api.model.ConfigMap
import io.fabric8.kubernetes.api.model.ObjectMetaBuilder
import io.fabric8.kubernetes.client.KubernetesClient
import io.fabric8.kubernetes.client.dsl.Resource
import kotlinx.serialization.KSerializer

/**
 * @author Niklas Nieberler
 */

class ConfigMapManager(
    private val namespace: String,
    private val kubernetesClient: KubernetesClient
) {

    private val cachedConfigMaps = hashMapOf<String, ConfigMap>()

    /**
     * Creates a new config map
     * @param name of the config
     * @param serializer of the config class
     * @param value what you want to save in the config
     */
    fun <C> createConfigMap(name: String, serializer: KSerializer<C>, value: C): C {
        val yamlString = Yaml.default.encodeToString(serializer, value)
        this.kubernetesClient.configMaps()
            .inNamespace(this.namespace)
            .resource(createConfigMap(name, yamlString))
            .create()
        return value
    }

    /**
     * Updates the cached config
     * @param name of the config
     */
    fun updateConfig(name: String) {
        this.cachedConfigMaps[name] = getConfigMapFromKubernetes(name).get()
    }

    /**
     * Returns true if this config is ready
     * @param name of the config
     * @return true or false :)
     */
    fun existsConfig(name: String): Boolean {
        return getConfigMapFromKubernetes(name).isReady
    }

    /**
     * Gets the config class of the [ConfigMap]
     * @param name of the config
     * @param serializer of the config class
     * @return config class instance
     */
    fun <C> getConfig(name: String, serializer: KSerializer<C>): C? {
        val configMap = this.cachedConfigMaps[name] ?: getConfigMapFromKubernetes(name).get()
        val string = configMap.data[name] ?: return null
        return Yaml.default.decodeFromString(serializer, string)
    }

    private fun getConfigMapFromKubernetes(name: String): Resource<ConfigMap> {
        return this.kubernetesClient.configMaps()
            .inNamespace(this.namespace)
            .withName(name)
    }

    private fun createConfigMap(name: String, yamlString: String): ConfigMap {
        val configMap = ConfigMap()
        configMap.metadata = ObjectMetaBuilder().withName(name).build()
        configMap.data = hashMapOf(Pair(name, yamlString))
        return configMap
    }

}