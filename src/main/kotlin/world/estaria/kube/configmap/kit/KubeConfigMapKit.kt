package world.estaria.kube.configmap.kit

import io.fabric8.kubernetes.client.KubernetesClientBuilder

/**
 * @author Niklas Nieberler
 */

object KubeConfigMapKit {

    fun initializeKubeConfig(): ConfigMapManager {
        val kubernetesClient = KubernetesClientBuilder().build()
        return ConfigMapManager(kubernetesClient)
    }

}