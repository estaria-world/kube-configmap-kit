package world.estaria.kube.configmap.kit

import io.fabric8.kubernetes.client.KubernetesClient
import io.fabric8.kubernetes.client.KubernetesClientBuilder

/**
 * @author Niklas Nieberler
 */

object KubeConfigMapKit {

    /**
     * Initialize a kubernetes client for the configMaps
     * @param namespace where the config is stored
     * @return new instance of [ConfigMapManager]
     */
    fun initializeKubeConfig(namespace: String): ConfigMapManager {
        val kubernetesClient = KubernetesClientBuilder().build()
        return ConfigMapManager(namespace, kubernetesClient)
    }

    /**
     * Initialize a kubernetes client for the configMaps
     * @param namespace where the config is stored
     * @param kubernetesClient the kubernetes client
     * @return new instance of [ConfigMapManager]
     */
    fun initializeKubeConfig(namespace: String, kubernetesClient: KubernetesClient): ConfigMapManager {
        return ConfigMapManager(namespace, kubernetesClient)
    }

}