package com.example.serviconnecta.feature.shared.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrivacyPolicyScreen(
    onNavigateBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Política de Privacidad") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, "Volver")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                "Política de Privacidad de ServiConnecta",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )

            Text(
                "Última actualización: Diciembre 2025",
                style = MaterialTheme.typography.bodySmall
            )

            Divider(modifier = Modifier.padding(vertical = 8.dp))

            SectionTitle("1. Información que Recopilamos")
            SectionContent(
                """
                En ServiConnecta recopilamos la siguiente información:

                • Información de Registro: Nombre completo, número de teléfono, correo electrónico y contraseña.
                • Información de Perfil: Fotografía de perfil, habilidades, experiencia, áreas de trabajo y servicios ofrecidos.
                • Información de Ubicación: Direcciones de servicio para facilitar la conexión entre clientes y trabajadores.
                • Información de Transacciones: Historial de servicios solicitados, completados y métodos de pago utilizados.
                • Reseñas y Calificaciones: Comentarios y valoraciones de servicios prestados.
                """.trimIndent()
            )

            SectionTitle("2. Uso de la Información")
            SectionContent(
                """
                Utilizamos su información para:

                • Facilitar la conexión entre clientes y proveedores de servicios.
                • Procesar solicitudes de servicio y pagos.
                • Mejorar la experiencia del usuario en la plataforma.
                • Enviar notificaciones sobre el estado de solicitudes y servicios.
                • Verificar la identidad de los trabajadores mediante documentación oficial.
                • Prevenir fraudes y garantizar la seguridad de la plataforma.
                • Analizar tendencias y realizar mejoras en nuestros servicios.
                """.trimIndent()
            )

            SectionTitle("3. Compartir Información")
            SectionContent(
                """
                ServiConnecta no vende su información personal. Compartimos información solo en los siguientes casos:

                • Con otros usuarios de la plataforma cuando sea necesario para facilitar un servicio (ej: ubicación del cliente con el trabajador).
                • Con procesadores de pago para completar transacciones.
                • Cuando sea requerido por ley o autoridades competentes.
                • Con su consentimiento explícito.
                """.trimIndent()
            )

            SectionTitle("4. Seguridad de Datos")
            SectionContent(
                """
                Implementamos medidas de seguridad para proteger su información:

                • Encriptación de datos sensibles durante la transmisión.
                • Almacenamiento seguro en servidores protegidos.
                • Acceso restringido a información personal solo a personal autorizado.
                • Verificación en dos pasos para cuentas de trabajadores.
                """.trimIndent()
            )

            SectionTitle("5. Sus Derechos")
            SectionContent(
                """
                Usted tiene derecho a:

                • Acceder a su información personal almacenada.
                • Solicitar la corrección de información incorrecta.
                • Eliminar su cuenta y datos asociados.
                • Oponerse al procesamiento de ciertos datos.
                • Solicitar una copia de sus datos en formato digital.

                Para ejercer estos derechos, contáctenos a través de la aplicación.
                """.trimIndent()
            )

            SectionTitle("6. Cookies y Tecnologías Similares")
            SectionContent(
                """
                Utilizamos cookies y tecnologías similares para:

                • Mantener su sesión activa.
                • Recordar sus preferencias.
                • Analizar el uso de la plataforma.
                • Mejorar la funcionalidad de la aplicación.
                """.trimIndent()
            )

            SectionTitle("7. Menores de Edad")
            SectionContent(
                """
                ServiConnecta está dirigido a usuarios mayores de 18 años. No recopilamos intencionalmente información de menores de edad.
                """.trimIndent()
            )

            SectionTitle("8. Cambios a esta Política")
            SectionContent(
                """
                Podemos actualizar esta Política de Privacidad ocasionalmente. Le notificaremos sobre cambios significativos a través de la aplicación o por correo electrónico.
                """.trimIndent()
            )

            SectionTitle("9. Contacto")
            SectionContent(
                """
                Si tiene preguntas sobre esta Política de Privacidad, contáctenos en:

                Email: privacidad@serviconnecta.com
                Teléfono: +51 999 888 777
                Dirección: Av. Javier Prado Este 123, San Isidro, Lima, Perú
                """.trimIndent()
            )

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
private fun SectionTitle(title: String) {
    Text(
        title,
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.Bold
    )
}

@Composable
private fun SectionContent(content: String) {
    Text(
        content,
        style = MaterialTheme.typography.bodyMedium
    )
}
