package world.estaria.kube.configmap.kit

import com.charleskorn.kaml.Yaml
import io.fabric8.kubernetes.api.model.ConfigMap
import io.fabric8.kubernetes.client.KubernetesClient
import kotlinx.serialization.KSerializer

/**
 * @author Niklas Nieberler
 */

class ConfigMapManager(
    private val kubernetesClient: KubernetesClient
) {

    private val cachedConfigMaps = hashMapOf<String, ConfigMap>()

    fun <C> migrateConfigToImage(name: String, serializer: KSerializer<C>) {

    }

    fun updateConfig(name: String) {
        this.cachedConfigMaps[name] = getConfigMapFromKubernetes(name)
    }

    fun <C> getConfig(name: String, serializer: KSerializer<C>): C {
        val configMap = this.cachedConfigMaps[name] ?: getConfigMapFromKubernetes(name)
        return Yaml.default.decodeFromString(serializer, "")
    }

    private fun getConfigMapFromKubernetes(name: String): ConfigMap {
        return this.kubernetesClient.configMaps()
            .inNamespace("default")
            .withName(name)
            .get()
    }

}