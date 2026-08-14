# BCP Móvil Lite

App de banca móvil (no oficial / proyecto de práctica) inspirada en el app del BCP, hecha 100% en **Jetpack Compose**. Incluye login, home con productos y movimientos, flujo completo de transferencias, detalle de tarjeta de crédito, perfil y soporte de tema claro/oscuro e idioma español/inglés.

> ⚠️ Proyecto de prueba ejercicios. No está afiliado al Banco de Crédito del Perú (BCP). Todos los datos (saldos, tarjetas, usuarios) son **mock**, no hay backend real.

## Stack técnico

- **Kotlin** + **Jetpack Compose** (UI declarativa, sin XML de layouts)
- **Material 3** (`androidx.compose.material3`)
- **Navigation Compose** (`androidx.navigation.compose`) para el ruteo entre pantallas
- **ViewModel + StateFlow** (`androidx.lifecycle.viewmodel.compose`) para el manejo de estado
- **SharedPreferences** para persistir preferencias simples (tema e idioma)
- `minSdk 24` / `targetSdk` y `compileSdk 35`

No usa Hilt, Retrofit ni Room: todo el "backend" es un objeto de datos mock (`MockData`), pensado como ejercicio de UI/UX y navegación.

## Arquitectura

Cada pantalla sigue similar al patrón **MVVM**:

```
ui/<feature>/
 ├─ <Feature>Screen.kt      -> Composable, solo UI, observa el ViewModel
 └─ <Feature>ViewModel.kt   -> estado (UiState) + lógica, expuesto como StateFlow
```

El `ViewModel` expone un único `data class ...UiState` inmutable vía `StateFlow`, y la `Screen` lo colecta con `collectAsState()`. Las acciones del usuario (click, texto, etc.) llaman a funciones del ViewModel, que actualizan el estado con `_uiState.update { ... }`. Ejemplo: `LoginViewModel` valida usuario/contraseña, simula una llamada de red con `delay()` y expone `isLoading`, `loginSuccess`, `loginError`.

Pantallas simples sin lógica propia (ej. `OperationsScreen`, `OnboardingScreen`) no tienen ViewModel y trabajan directo con los mocks o callbacks.

### Flujo de transferencias (ViewModel compartido)

El flujo `Transferir` (`TransferType → TransferForm → TransferConfirm → TransferReceipt`) es un **grafo de navegación anidado** (`navigation(...)` dentro de `NavGraph.kt`) donde los 4 pasos comparten **una misma instancia** de `TransferViewModel`, obtenida así:

```kotlin
private fun NavBackStackEntry.transferViewModel(navController: NavHostController): TransferViewModel {
    val parentEntry = remember(this) { navController.getBackStackEntry(Screen.TransferGraph.route) }
    return viewModel(parentEntry)
}
```

Esto permite que el tipo de transferencia elegido en el paso 1 esté disponible en el resumen del paso 4, sin pasar argumentos por la ruta ni usar un ViewModel global.

## Estructura del proyecto

```
app/src/main/java/com/example/misejercicios/
├─ MainActivity.kt              # entry point: tema, idioma (attachBaseContext) y NavHost
├─ data/
│  ├─ AppPreferences.kt         # SharedPreferences: tema oscuro/claro e idioma
│  └─ mock/MockData.kt          # productos, movimientos, tarjeta, perfil, credenciales mock
└─ ui/
   ├─ navigation/NavGraph.kt    # todas las rutas (sealed class Screen) + NavHost
   ├─ theme/                    # Color.kt, Theme.kt, Type.kt (Material 3 light/dark)
   ├─ components/               # botones, textfields, top/bottom bar, cards, etc. reutilizables
   ├─ splash/                   # SplashScreen
   ├─ onboarding/                
   ├─ login/                    # LoginScreen + RecoveryScreen (recuperar contraseña)
   ├─ home/                     # Home: saldo, accesos rápidos, últimos movimientos
   ├─ operations/                
   ├─ cards/                    # Lista de tarjetas + detalle (consumos, datos de la tarjeta)
   ├─ transfer/                 # Flujo de 4 pasos descrito arriba
   ├─ profile/                   
   └─ more/                     # menú "Más" (perfil, logout, etc.)
```

## Rutas (navegación)

Definidas como `sealed class Screen` en [`NavGraph.kt`](app/src/main/java/com/example/misejercicios/ui/navigation/NavGraph.kt):

| Ruta | Pantalla | Notas |
|---|---|---|
| `splash` | `SplashScreen` | destino inicial del `NavHost` |
| `onboarding` | `OnboardingScreen` | se muestra una sola vez, hace `popUpTo` inclusive al salir |
| `login` | `LoginScreen` | credenciales mock: ver más abajo |
| `recovery` | `RecoveryScreen` | recuperar contraseña |
| `home` | `HomeScreen` | tab del bottom bar |
| `operations` | `OperationsScreen` | tab del bottom bar |
| `cards` | `CardsListScreen` | tab del bottom bar |
| `more` | `MoreScreen` | tab del bottom bar |
| `profile` | `ProfileScreen` | accedida desde "Más" |
| `card_detail/{cardId}` | `CardDetailScreen` | recibe `cardId` como argumento de ruta |
| `transfer_graph` (grafo anidado) | — | contenedor del flujo de transferencia |
| ├─ `transfer_type` | `TransferTypeScreen` | elegir tipo (entre cuentas propias, a BCP, interbancaria, Yape) |
| ├─ `transfer_form` | `TransferFormScreen` | datos de la transferencia |
| ├─ `transfer_confirm` | `TransferConfirmScreen` | confirmación/autorización |
| └─ `transfer_receipt` | `TransferReceiptScreen` | comprobante final |

El **bottom bar** (`Home`, `Operaciones`, `Tarjetas`, `Más`) solo se muestra en esas 4 rutas; el resto de pantallas ocupa toda la pantalla. Las transiciones del flujo de transferencia usan slide horizontal (`slideInHorizontally`/`slideOutHorizontally`) en vez del fade por defecto.

## Funcionalidades

- **Login** con validación de campos y credenciales mock (`ivan.perez` / `Password123`), delay simulado de red y manejo de errores.
- **Home**: saldo/resumen de productos, accesos rápidos, últimos movimientos, toggle de tema y de idioma.
- **Tarjetas**: listado y detalle con consumos, línea de crédito, fecha de corte/pago.
- **Transferencias**: flujo guiado de 4 pasos con estado compartido entre pantallas.
- **Perfil**: datos del usuario, logout (vuelve a `login` limpiando el back stack).
- **Preferencias**: tema claro/oscuro e idioma (ES/EN) persistidos en `SharedPreferences`, sin depender del idioma del sistema.

## Datos mock

No hay backend: todo vive en [`MockData.kt`](app/src/main/java/com/example/misejercicios/data/mock/MockData.kt) (productos, movimientos, cuentas, beneficiarios frecuentes, tarjeta de crédito, consumos y perfil de usuario).

```
Usuario:    ivan.perez
Contraseña: Password123
```

## Cómo correr el proyecto

1. Abrir la carpeta en Android Studio (Ladybug o superior recomendado).
2. Dejar que sincronice Gradle (usa el catálogo de versiones `libs.versions.toml`).
3. Ejecutar en un emulador o dispositivo con Android 7.0 (API 24) o superior.
