package com.christian.nimbo.constants;

public final class DiagnosisMessages {

    private DiagnosisMessages() {
        // Evita crear instancias de esta clase
    }

    //region GASTOS
    public static final String EXPENSES_EXCELLENT =
            "Tus gastos están claramente por debajo de tus ingresos. "
                    + "Ese margen te da libertad real: puedes reforzar tu seguridad, invertir con calma o destinarlo a lo que de verdad te importa.";

    public static final String EXPENSES_GOOD =
            "Mantienes los gastos bajo control y consigues conservar una parte relevante de lo que ganas. "
                    + "El siguiente paso natural es convertir ese margen en una estrategia clara de seguridad y crecimiento.";

    public static final String EXPENSES_MODERATE =
            "Los gastos se llevan una porción importante de tus ingresos. "
                    + "Aún tienes capacidad de maniobra, pero cada euro que consigas liberar cada mes marcará una diferencia notable en la velocidad a la que avances.";

    public static final String EXPENSES_HIGH =
            "Una proporción elevada de tus ingresos se va en gastos. "
                    + "Eso reduce de forma significativa el dinero disponible para crear colchón o invertir, y hace que cualquier imprevisto pese más de lo deseable.";

    public static final String EXPENSES_VERY_HIGH =
            "Más del 80 % de tus ingresos se destina a gastos. "
                    + "Con un margen tan estrecho, cualquier contratiempo puede desequilibrar rápidamente tus finanzas. Recuperar capacidad mensual es, ahora mismo, una de las palancas más potentes que tienes.";
    //endregion

    //region FONDO DE EMERGENCIA
    public static final String EMERGENCY_ZERO =
            "Ahora mismo no cuentas con un colchón que pueda cubrir tus gastos si dejan de entrar ingresos. "
                    + "Antes de depender de las inversiones para salir de un apuro, conviene priorizar la construcción de una primera reserva.";

    public static final String EMERGENCY_UNDER_TWO_MONTHS =
            "Ya tienes una pequeña reserva, aunque todavía cubre menos de dos meses de gastos. "
                    + "Es un primer paso, pero sigue siendo una protección limitada. Ampliarla te dará más margen para no tener que alterar tus planes ante un imprevisto.";

    public static final String EMERGENCY_TWO_TO_THREE_MONTHS =
            "Tu colchón cubre entre dos y tres meses de gastos. "
                    + "Ya puedes absorber algunos contratiempos con mayor tranquilidad, aunque todavía tienes margen para reforzar la protección frente a una interrupción prolongada de ingresos.";

    public static final String EMERGENCY_THREE_TO_FOUR_MONTHS =
            "Cuentas con entre tres y cuatro meses de gastos cubiertos. "
                    + "Es una base razonable de seguridad y ya tienes una protección significativa, aunque todavía estás a medio camino del colchón de seis meses que usamos como referencia.";

    public static final String EMERGENCY_FOUR_TO_SIX_MONTHS =
            "Tienes entre cuatro y seis meses de gastos cubiertos. "
                    + "La protección ya es sólida y cada mes adicional refuerza una seguridad que ya existe, acercándote a la referencia de seis meses.";

    public static final String EMERGENCY_SIX_PLUS_MONTHS =
            "Tienes cubiertos seis meses o más de gastos. "
                    + "Has alcanzado la referencia de seguridad que utilizamos. A partir de aquí, la atención puede centrarse en cómo hacer trabajar el dinero que generas una vez cubiertas tus necesidades.";
    //endregion

    //region INVERSIÓN
    public static final String INVESTMENT_NONE =
            "Prácticamente no estás destinando parte de tus ingresos a inversión. "
                    + "Eso significa que la mayor parte de tu capacidad financiera aún no está participando en la construcción de patrimonio a largo plazo.";

    public static final String INVESTMENT_LOW =
            "Estás dando los primeros pasos en inversión, aunque todavía representa una porción pequeña de tus ingresos. "
                    + "La clave ahora es mantener la aportación y, cuando sea posible, ir aumentándola de forma sostenible.";

    public static final String INVESTMENT_MODERATE_LOW =
            "Ya destinas una cantidad apreciable a inversión. "
                    + "Tienes una base sobre la que construir; a partir de aquí la constancia suele importar más que intentar acertar el momento perfecto.";

    public static final String INVESTMENT_MODERATE =
            "Una parte significativa de tus ingresos ya está trabajando en inversión. "
                    + "Con aportaciones regulares, el tiempo puede convertirse en un aliado importante para tu patrimonio.";

    public static final String INVESTMENT_HIGH =
            "Estás dedicando una proporción elevada de tus ingresos a inversión. "
                    + "Es una capacidad interesante de construir patrimonio, siempre que vaya acompañada de una reserva suficiente para no verte obligado a vender en un mal momento.";

    public static final String INVESTMENT_VERY_HIGH =
            "Al menos un 10 % de tus ingresos va a inversión. "
                    + "La aportación ya tiene un peso relevante. Mantenerla de forma sostenible suele ser más valioso que intentar incrementarla sin límite.";
//endregion

    //region DEUDA
    public static final String DEBT_NONE =
            "No tienes deuda dentro del perímetro que evaluamos. "
                    + "Eso deja libre una parte mayor de tu capacidad para construir seguridad y patrimonio.";

    public static final String DEBT_LOW =
            "Tu deuda es reducida en relación con tus ingresos anuales. "
                    + "No condiciona de forma relevante tu situación, aunque conviene no perderla de vista mientras avanzas en otros objetivos.";

    public static final String DEBT_MODERATE =
            "La deuda ya representa una parte apreciable de tus ingresos anuales. "
                    + "No implica necesariamente un problema, pero sí reduce el margen que puedes dedicar a otros frentes.";

    public static final String DEBT_HIGH =
            "La proporción de deuda respecto a tus ingresos anuales es elevada. "
                    + "Eso puede limitar tu capacidad para afrontar imprevistos y construir patrimonio al mismo tiempo.";

    public static final String DEBT_VERY_HIGH =
            "Tu deuda supera una proporción muy alta de tus ingresos anuales. "
                    + "Con una carga así, una parte importante de tu capacidad financiera queda comprometida antes de poder destinarla a otros objetivos.";
    //endregion

    //region SITUACIONES COMBINADAS
    public static final String NO_EMERGENCY_WITH_INVESTMENT =
            "Estás invirtiendo, pero todavía no cuentas con una red de seguridad que proteja ese esfuerzo. "
                    + "Un imprevisto relevante podría obligarte a tocar o vender parte de lo que estás construyendo.";

    public static final String LOW_EMERGENCY_WITH_HIGH_INVESTMENT =
            "Estás haciendo un esfuerzo importante por invertir mientras tu colchón sigue siendo reducido. "
                    + "Parte de ese esfuerzo queda expuesto si aparece un gasto inesperado o una caída de ingresos.";

    public static final String THREE_MONTHS_WITH_INVESTMENT =
            "Tienes una base de unos tres meses de seguridad y, además, estás invirtiendo. "
                    + "Es un buen punto de partida para combinar protección y crecimiento, aunque todavía puedes reforzar el colchón antes de depender más de las inversiones.";

    public static final String SIX_MONTHS_WITH_INVESTMENT =
            "Cuentas con un colchón de al menos seis meses y además estás invirtiendo. "
                    + "La base de seguridad está cubierta; el foco puede desplazarse con naturalidad hacia la construcción sostenida de patrimonio.";

    public static final String SIX_MONTHS_WITHOUT_INVESTMENT =
            "Tu colchón ya cubre seis meses o más de gastos. Has construido una base sólida de seguridad. "
                    + "La principal oportunidad que queda es decidir qué parte de tu capacidad disponible quieres empezar a dirigir hacia la construcción de patrimonio.";

    public static final String HIGH_EXPENSES_WITH_NO_EMERGENCY =
            "Tus gastos se llevan una parte muy alta de tus ingresos y, además, no tienes colchón. "
                    + "Las dos cosas se refuerzan: poco margen mensual y ninguna reserva que lo proteja.";

    public static final String HIGH_EXPENSES_WITH_EMERGENCY =
            "Aunque dispones de un colchón que te protege, tus gastos siguen absorbiendo una proporción elevada de tus ingresos. "
                    + "Reducir parte de ese gasto liberaría capacidad para seguir construyendo patrimonio sin necesidad de ingresos más altos.";

    public static final String LOW_EXPENSES_WITHOUT_EMERGENCY =
            "Tus gastos dejan un margen amplio, pero todavía no has transformado ese margen en una reserva de seguridad. "
                    + "La situación tiene potencial de mejora rápida si consigues convertir parte de ese excedente en colchón.";

    public static final String LOW_EXPENSES_WITH_FULL_EMERGENCY =
            "Gastos controlados y colchón de al menos seis meses. "
                    + "Has construido una base con bastante margen de maniobra. La siguiente decisión es cómo hacer trabajar el excedente.";

    public static final String HIGH_DEBT_WITHOUT_EMERGENCY =
            "La deuda pesa de forma relevante sobre tus ingresos y, además, no tienes un colchón suficiente. "
                    + "La combinación hace que una interrupción de ingresos pueda tener un impacto especialmente fuerte.";

    public static final String HIGH_DEBT_WITH_INVESTMENT =
            "Estás invirtiendo mientras mantienes una carga de deuda elevada. "
                    + "Conviene valorar el coste de esa deuda, tu capacidad real para afrontar imprevistos y el horizonte de tus inversiones antes de mantener ambas cosas al mismo tiempo.";

    public static final String NO_DEBT_WITH_FULL_EMERGENCY =
            "Sin deuda relevante y con al menos seis meses de gastos cubiertos. "
                    + "Has eliminado dos de las principales fuentes de presión financiera. Puedes centrarte con más claridad en construir patrimonio a largo plazo.";

    public static final String NO_DEBT_WITH_INVESTMENT =
            "Sin deuda dentro del perímetro evaluado y, además, invirtiendo parte de tus ingresos. "
                    + "Eso permite que una proporción mayor de tu capacidad financiera se dirija directamente a construir patrimonio.";
    //endregion
}
