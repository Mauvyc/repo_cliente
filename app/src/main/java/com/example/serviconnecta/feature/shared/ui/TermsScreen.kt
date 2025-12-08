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
fun TermsScreen(
    onNavigateBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Términos y Condiciones") },
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
                "Términos y Condiciones de ServiConnecta",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )

            Text(
                "Última actualización: Diciembre 2025",
                style = MaterialTheme.typography.bodySmall
            )

            Divider(modifier = Modifier.padding(vertical = 8.dp))

            SectionTitle("1. Aceptación de los Términos")
            SectionContent(
                """
                Al acceder y utilizar ServiConnecta, usted acepta cumplir con estos Términos y Condiciones. Si no está de acuerdo con alguna parte de estos términos, no debe usar la plataforma.

                ServiConnecta es una plataforma que conecta a clientes con proveedores de servicios profesionales en áreas de electricidad, gasfitería y albañilería en Lima, Perú.
                """.trimIndent()
            )

            SectionTitle("2. Registro y Cuenta de Usuario")
            SectionContent(
                """
                • Debe ser mayor de 18 años para registrarse.
                • Debe proporcionar información veraz y actualizada.
                • Es responsable de mantener la confidencialidad de su contraseña.
                • Debe notificar inmediatamente cualquier uso no autorizado de su cuenta.
                • Los trabajadores deben verificar su identidad mediante documentación oficial.
                • Cada usuario solo puede tener una cuenta activa.
                """.trimIndent()
            )

            SectionTitle("3. Uso de la Plataforma")
            SectionContent(
                """
                Como usuario de ServiConnecta, usted se compromete a:

                • Utilizar la plataforma solo para fines legales.
                • No publicar contenido ofensivo, fraudulento o engañoso.
                • No suplantar la identidad de otra persona.
                • Respetar los derechos de propiedad intelectual.
                • No interferir con el funcionamiento de la plataforma.
                • Mantener una comunicación respetuosa con otros usuarios.
                """.trimIndent()
            )

            SectionTitle("4. Servicios de Trabajadores")
            SectionContent(
                """
                Los trabajadores registrados se comprometen a:

                • Proporcionar servicios profesionales de calidad.
                • Cumplir con los horarios acordados.
                • Utilizar materiales y herramientas adecuadas.
                • Contar con las habilidades y experiencia declaradas.
                • Respetar la propiedad y privacidad del cliente.
                • Comunicar cualquier problema o retraso de manera oportuna.
                • Mantener precios transparentes y acordados previamente.
                """.trimIndent()
            )

            SectionTitle("5. Responsabilidades del Cliente")
            SectionContent(
                """
                Los clientes se comprometen a:

                • Proporcionar información precisa sobre el servicio requerido.
                • Estar presente en la ubicación acordada a la hora indicada.
                • Proporcionar acceso adecuado al área de trabajo.
                • Respetar al trabajador y sus herramientas.
                • Pagar el monto acordado por el servicio.
                • Dejar reseñas honestas y constructivas.
                """.trimIndent()
            )

            SectionTitle("6. Pagos y Tarifas")
            SectionContent(
                """
                • Los precios de servicios son establecidos por cada trabajador.
                • ServiConnecta puede cobrar una comisión por transacción.
                • Los pagos deben realizarse a través de los métodos autorizados en la plataforma.
                • Los trabajadores recibirán su pago después de la confirmación del servicio.
                • En caso de disputa, el pago puede ser retenido hasta su resolución.
                • Los reembolsos se manejan caso por caso según nuestra política.
                """.trimIndent()
            )

            SectionTitle("7. Cancelaciones y Reembolsos")
            SectionContent(
                """
                • Los clientes pueden cancelar servicios con al menos 24 horas de anticipación.
                • Cancelaciones de último momento pueden estar sujetas a penalidades.
                • Los trabajadores pueden cancelar en casos de emergencia justificada.
                • Los reembolsos se procesarán según la política de cancelación.
                • ServiConnecta se reserva el derecho de mediar en disputas.
                """.trimIndent()
            )

            SectionTitle("8. Reseñas y Calificaciones")
            SectionContent(
                """
                • Las reseñas deben ser honestas y basadas en experiencias reales.
                • No se permiten reseñas falsas o manipuladas.
                • ServiConnecta se reserva el derecho de eliminar reseñas inapropiadas.
                • Las calificaciones afectan la visibilidad de los trabajadores en la plataforma.
                • Los trabajadores no pueden solicitar reseñas falsas a cambio de beneficios.
                """.trimIndent()
            )

            SectionTitle("9. Limitación de Responsabilidad")
            SectionContent(
                """
                ServiConnecta actúa como intermediario y no es responsable por:

                • La calidad de los servicios prestados.
                • Daños o pérdidas durante la prestación del servicio.
                • Disputas entre clientes y trabajadores.
                • Accidentes o lesiones durante el servicio.
                • Garantías sobre el trabajo realizado (estas son responsabilidad del trabajador).

                Los usuarios acuerdan indemnizar a ServiConnecta contra cualquier reclamo derivado del uso de la plataforma.
                """.trimIndent()
            )

            SectionTitle("10. Suspensión y Terminación")
            SectionContent(
                """
                ServiConnecta se reserva el derecho de:

                • Suspender o terminar cuentas que violen estos términos.
                • Eliminar contenido inapropiado sin previo aviso.
                • Modificar o discontinuar servicios de la plataforma.
                • Investigar y reportar actividades sospechosas a las autoridades.

                Los usuarios pueden cerrar sus cuentas en cualquier momento desde la configuración.
                """.trimIndent()
            )

            SectionTitle("11. Propiedad Intelectual")
            SectionContent(
                """
                • El contenido de ServiConnecta (logo, diseño, textos) está protegido por derechos de autor.
                • Los usuarios mantienen los derechos sobre el contenido que publican.
                • Al publicar contenido, otorgas a ServiConnecta una licencia para usarlo en la plataforma.
                • No está permitido copiar, modificar o distribuir el contenido de la plataforma sin autorización.
                """.trimIndent()
            )

            SectionTitle("12. Modificaciones a los Términos")
            SectionContent(
                """
                ServiConnecta puede modificar estos términos en cualquier momento. Los cambios significativos se notificarán con 30 días de anticipación. El uso continuado de la plataforma después de los cambios constituye la aceptación de los nuevos términos.
                """.trimIndent()
            )

            SectionTitle("13. Ley Aplicable")
            SectionContent(
                """
                Estos términos se rigen por las leyes de la República del Perú. Cualquier disputa será resuelta en los tribunales de Lima, Perú.
                """.trimIndent()
            )

            SectionTitle("14. Contacto")
            SectionContent(
                """
                Para preguntas sobre estos Términos y Condiciones:

                Email: soporte@serviconnecta.com
                Teléfono: +51 999 888 777
                Dirección: Av. Javier Prado Este 123, San Isidro, Lima, Perú
                Horario de atención: Lunes a Viernes, 9:00 AM - 6:00 PM
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
