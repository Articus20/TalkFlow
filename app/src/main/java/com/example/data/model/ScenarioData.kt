package com.example.data.model

object ScenarioData {

    val coffee = PracticeScenario(
        id = "coffee",
        title = "Ordering coffee",
        level = ScenarioLevel.BASIC,
        durationMinutes = 4,
        category = "Everyday",
        persona = Persona(
            id = "sam",
            name = "Sam Carter",
            firstName = "Sam",
            role = "Barista · Café Order",
            accent = "US English · Warm & Friendly"
        ),
        targetKeywords = listOf("medium", "oat milk", "to go", "receipt"),
        extraKeywords = listOf("extra hot", "for here", "pastry"),
        vocabulary = listOf(
            VocabWord("to go", "para llevar"),
            VocabWord("medium", "mediano"),
            VocabWord("receipt", "recibo / comprobante"),
            VocabWord("oat milk", "leche de avena"),
            VocabWord("pastry", "pan dulce o repostería")
        ),
        idioms = listOf(
            NativeIdiom("A cup of joe", "Casual American way to say a cup of coffee."),
            NativeIdiom("Hit the spot", "Use when a drink or meal is exactly what you needed.")
        ),
        turns = listOf(
            DialogTurn(
                botEnglish = "Hi there! Welcome in. What can I get started for you today?",
                botSpanish = "¡Hola! Bienvenidos. ¿Qué te preparo hoy?",
                panic = PanicSuggestion(
                    userSpanish = "Quiero un café con leche, por favor.",
                    translatedEnglish = "I'd like a latte, please.",
                    conservative = "Could I have a medium latte, please?",
                    natural = "Hi! Can I get a medium latte, please?",
                    advanced = "Hi there — I'd love a medium oat-milk latte, extra hot if possible."
                )
            ),
            DialogTurn(
                botEnglish = "Great choice! Would you like that hot or iced, and any milk preference?",
                botSpanish = "¡Excelente elección! ¿Lo quieres caliente o frío, y qué tipo de leche?",
                panic = PanicSuggestion(
                    userSpanish = "Caliente y con leche de avena, por favor.",
                    translatedEnglish = "Hot, with oat milk, please.",
                    conservative = "Hot, please, with oat milk.",
                    natural = "Hot, and oat milk if you have it.",
                    advanced = "Hot, please — and I'll go with oat milk, if that's not a hassle."
                )
            ),
            DialogTurn(
                botEnglish = "Perfect. Anything else, like a pastry? And is that for here or to go?",
                botSpanish = "Perfecto. ¿Algo más, como un pan dulce? ¿Es para tomar aquí o para llevar?",
                panic = PanicSuggestion(
                    userSpanish = "Para llevar, y nada más, gracias.",
                    translatedEnglish = "To go, and nothing else, thanks.",
                    conservative = "To go, please. That will be all, thank you.",
                    natural = "To go, and that is everything, thanks!",
                    advanced = "To go, please — that'll be all. Could I get a receipt as well?"
                )
            )
        ),
        closingEnglish = "That's $5.75. Here's your freshly brewed drink — have a lovely day!",
        closingSpanish = "Son $5.75. Aquí tienes tu bebida recién hecha — ¡que tengas un lindo día!"
    )

    val tickets = PracticeScenario(
        id = "tickets",
        title = "Buying tickets",
        level = ScenarioLevel.BASIC,
        durationMinutes = 6,
        category = "Travel",
        persona = Persona(
            id = "jamie",
            name = "Jamie Lee",
            firstName = "Jamie",
            role = "Ticket Agent · Train Station",
            accent = "US English · Clear & Calm"
        ),
        targetKeywords = listOf("round-trip", "window seat", "departure", "fare"),
        extraKeywords = listOf("platform", "one-way", "schedule"),
        vocabulary = listOf(
            VocabWord("round-trip", "ida y vuelta"),
            VocabWord("one-way", "solo ida"),
            VocabWord("fare", "tarifa / precio"),
            VocabWord("platform", "andén"),
            VocabWord("departure", "salida / hora de partida")
        ),
        idioms = listOf(
            NativeIdiom("Hop on board", "Casual way to say get onto the train or bus."),
            NativeIdiom("Right on time", "Use when something arrives or departs exactly on schedule.")
        ),
        turns = listOf(
            DialogTurn(
                botEnglish = "Good morning. Where are you traveling to today?",
                botSpanish = "Buenos días. ¿A dónde viajas hoy?",
                panic = PanicSuggestion(
                    userSpanish = "Necesito un boleto a Boston para mañana en la mañana.",
                    translatedEnglish = "I need a ticket to Boston for tomorrow morning.",
                    conservative = "I'd like a ticket to Boston for tomorrow morning, please.",
                    natural = "Hi, I need a ticket to Boston tomorrow morning.",
                    advanced = "Good morning — I'd like to book a ticket to Boston for tomorrow morning, please."
                )
            ),
            DialogTurn(
                botEnglish = "Do you want one-way or round-trip? Morning departures are 8:10 and 10:40.",
                botSpanish = "¿Ida o ida y vuelta? Las salidas de la mañana son a las 8:10 y 10:40.",
                panic = PanicSuggestion(
                    userSpanish = "Ida y vuelta, saliendo a las 8:10.",
                    translatedEnglish = "Round-trip, leaving at 8:10.",
                    conservative = "Round-trip, please, on the 8:10 departure.",
                    natural = "Round-trip, and the 8:10 works best for me.",
                    advanced = "I'll take a round-trip on the 8:10 — could you check flexible fares?"
                )
            ),
            DialogTurn(
                botEnglish = "Got it. The flexible fare is $48. Would you prefer a window seat?",
                botSpanish = "Listo. La tarifa flexible son $48. ¿Prefieres asiento de ventana?",
                panic = PanicSuggestion(
                    userSpanish = "Sí, ventana, y pago con tarjeta de crédito.",
                    translatedEnglish = "Yes, window, and I'll pay by credit card.",
                    conservative = "Yes, a window seat, please. I'll pay by card.",
                    natural = "A window seat would be great — I'll pay by card.",
                    advanced = "A window seat would be ideal, thanks. I'll pay by card, and please email the ticket."
                )
            )
        ),
        closingEnglish = "All set! Your train departs from Platform 4. Have a wonderful trip!",
        closingSpanish = "¡Todo listo! Tu tren sale del andén 4. ¡Que tengas un viaje maravilloso!"
    )

    val interview = PracticeScenario(
        id = "interview",
        title = "Job interviews",
        level = ScenarioLevel.INTERMEDIATE,
        durationMinutes = 8,
        category = "Career",
        persona = Persona(
            id = "alex",
            name = "Alex Rivera",
            firstName = "Alex",
            role = "Hiring Manager · Product Interview",
            accent = "US English · Warm & Professional"
        ),
        targetKeywords = listOf("impact", "ownership", "collaborate", "trade-off"),
        extraKeywords = listOf("leverage", "align", "metric"),
        vocabulary = listOf(
            VocabWord("trade-off", "compromiso / balance entre dos opciones"),
            VocabWord("align", "alinear objetivos con el equipo"),
            VocabWord("leverage", "aprovechar una ventaja o recurso"),
            VocabWord("ownership", "asumir la responsabilidad total"),
            VocabWord("impact", "impacto cuantificable")
        ),
        idioms = listOf(
            NativeIdiom("Move the needle", "Make a noticeable difference in measurable results."),
            NativeIdiom("Make the trade-off", "Choose between two conflicting priorities deliberately.")
        ),
        turns = listOf(
            DialogTurn(
                botEnglish = "Thanks for joining today. Tell me about a time you had to balance speed with quality.",
                botSpanish = "Gracias por unirte hoy. Cuéntame sobre una ocasión en la que tuviste que equilibrar rapidez con calidad.",
                panic = PanicSuggestion(
                    userSpanish = "Me cuesta priorizar cuando todo parece urgente.",
                    translatedEnglish = "I find it hard to prioritize when everything feels urgent.",
                    conservative = "I prioritize tasks by urgency and impact, then confirm expectations with my team.",
                    natural = "I first clarify what truly moves the work forward, then communicate the trade-offs early.",
                    advanced = "I distinguish urgency from impact, align stakeholders, and protect the highest-leverage outcome."
                )
            ),
            DialogTurn(
                botEnglish = "Interesting approach. How did you take ownership of the outcome when the deadline moved?",
                botSpanish = "Enfoque interesante. ¿Cómo asumiste la responsabilidad cuando se adelantó la fecha límite?",
                panic = PanicSuggestion(
                    userSpanish = "Hablé con mi equipo y recortamos lo que no era esencial.",
                    translatedEnglish = "I talked with my team and we cut what wasn't essential.",
                    conservative = "I spoke with my team and we removed the non-essential tasks to deliver on time.",
                    natural = "I got the team together and we cut the scope down to what mattered most.",
                    advanced = "I aligned the team on a lean scope, owned the trade-off, and kept stakeholders informed daily."
                )
            ),
            DialogTurn(
                botEnglish = "And what would you say is your biggest strength when collaborating with cross-functional teams?",
                botSpanish = "¿Y cuál dirías que es tu mayor fortaleza al colaborar con equipos multidisciplinarios?",
                panic = PanicSuggestion(
                    userSpanish = "Escucho bien y comunico con mucha claridad.",
                    translatedEnglish = "I listen well and communicate clearly.",
                    conservative = "I am a proactive listener and I communicate expectations clearly.",
                    natural = "I listen first, then make sure everyone is completely on the same page.",
                    advanced = "I listen actively and translate different viewpoints into a shared plan everyone can commit to."
                )
            )
        ),
        closingEnglish = "Thank you so much — that was a very insightful conversation. We will be in touch with next steps soon!",
        closingSpanish = "Muchas gracias — fue una conversación muy enriquecedora. ¡Nos pondremos en contacto pronto!"
    )

    val hotel = PracticeScenario(
        id = "hotel",
        title = "Hotel claims",
        level = ScenarioLevel.INTERMEDIATE,
        durationMinutes = 7,
        category = "Travel & Hospitality",
        persona = Persona(
            id = "priya",
            name = "Priya Nair",
            firstName = "Priya",
            role = "Front Desk Manager · Luxury Hotel",
            accent = "US English · Formal & Courteous"
        ),
        targetKeywords = listOf("refund", "inconvenience", "compensation", "resolve"),
        extraKeywords = listOf("discount", "upgrade", "amenities"),
        vocabulary = listOf(
            VocabWord("compensation", "compensación / indemnización"),
            VocabWord("inconvenience", "molestia o inconveniente"),
            VocabWord("refund", "reembolso"),
            VocabWord("resolve", "resolver una queja"),
            VocabWord("complimentary", "de cortesía / gratuito")
        ),
        idioms = listOf(
            NativeIdiom("Meet me halfway", "Propose an agreeable compromise."),
            NativeIdiom("Go the extra mile", "Do much more than the bare minimum expected.")
        ),
        turns = listOf(
            DialogTurn(
                botEnglish = "Good evening. I understand there is an issue with your room amenities. How may I assist you?",
                botSpanish = "Buenas noches. Entiendo que hay un problema con las comodidades de su habitación. ¿En qué le puedo asistir?",
                panic = PanicSuggestion(
                    userSpanish = "El aire acondicionado no funciona desde ayer y no pude dormir bien.",
                    translatedEnglish = "The air conditioning hasn't worked since yesterday and I couldn't sleep.",
                    conservative = "The air conditioning in my room hasn't been working since yesterday, which was very disruptive.",
                    natural = "Hi, the A/C in my room has been broken since yesterday, so I barely got any sleep.",
                    advanced = "I'd like to report that the air conditioning has been out since yesterday, severely compromising my stay."
                )
            ),
            DialogTurn(
                botEnglish = "I sincerely apologize for the discomfort. We can relocate you to an upgraded suite tonight. Would that work?",
                botSpanish = "Me disculpo sinceramente por la molestia. Podemos reubicarlo en una suite superior esta noche. ¿Le parece bien?",
                panic = PanicSuggestion(
                    userSpanish = "Sí, pero también quiero un descuento por la molestia.",
                    translatedEnglish = "Yes, but I'd also like a discount for the inconvenience.",
                    conservative = "That works, but could we also discuss a discount for the inconvenience?",
                    natural = "A new suite is fine, but I'd also appreciate some compensation for the trouble.",
                    advanced = "A room transfer works, though I would expect a partial refund or rate credit given two restless nights."
                )
            ),
            DialogTurn(
                botEnglish = "I can apply a 25% credit to tonight's stay and offer complimentary breakfast. Would that be acceptable?",
                botSpanish = "Puedo aplicar un 25% de crédito a la tarifa de hoy y ofrecer desayuno de cortesía. ¿Sería aceptable?",
                panic = PanicSuggestion(
                    userSpanish = "Me parece justo, muchas gracias por resolverlo.",
                    translatedEnglish = "That seems fair, thank you very much for resolving it.",
                    conservative = "That sounds reasonable. Thank you for resolving this so promptly.",
                    natural = "That's very fair — I appreciate you sorting this out for me.",
                    advanced = "I appreciate that resolution; that feels balanced and I look forward to the rest of the stay."
                )
            )
        ),
        closingEnglish = "Wonderful. I have updated your folio and your new keycards are ready. Thank you for your gracious patience.",
        closingSpanish = "Maravilloso. He actualizado su cuenta y sus nuevas tarjetas llave están listas. Gracias por su paciencia."
    )

    val negotiation = PracticeScenario(
        id = "negotiation",
        title = "Negotiations",
        level = ScenarioLevel.ADVANCED,
        durationMinutes = 10,
        category = "Business",
        persona = Persona(
            id = "jordan",
            name = "Jordan Blake",
            firstName = "Jordan",
            role = "Vendor Lead · Contract Negotiations",
            accent = "US English · Direct & Strategic"
        ),
        targetKeywords = listOf("leverage", "commitment", "flexibility", "terms"),
        extraKeywords = listOf("upfront", "quarterly", "deliverables"),
        vocabulary = listOf(
            VocabWord("leverage", "ventaja o poder de negociación"),
            VocabWord("commitment", "compromiso contractual"),
            VocabWord("terms", "condiciones y cláusulas"),
            VocabWord("upfront", "pago por adelantado"),
            VocabWord("floor", "precio o condición mínima aceptable")
        ),
        idioms = listOf(
            NativeIdiom("Meet in the middle", "Settle on a midpoint compromise."),
            NativeIdiom("Put it on the table", "Bring a specific proposal forward for consideration.")
        ),
        turns = listOf(
            DialogTurn(
                botEnglish = "Our standard enterprise tier starts at $12,000 monthly. What flexibility were you hoping to discuss?",
                botSpanish = "Nuestro plan corporativo estándar inicia en $12,000 mensuales. ¿Qué margen de flexibilidad deseaban discutir?",
                panic = PanicSuggestion(
                    userSpanish = "Necesitamos una tarifa menor si firmamos por un contrato anual.",
                    translatedEnglish = "We need a lower price if we sign an annual contract.",
                    conservative = "We would appreciate a lower monthly rate if we commit to a twelve-month agreement.",
                    natural = "If we sign for a full year, could we bring that monthly rate down?",
                    advanced = "Given a twelve-month commitment, we'd expect volume-based elasticity in the monthly recurring fee."
                )
            ),
            DialogTurn(
                botEnglish = "We could adjust down to $10,800, but only with upfront annual payment. How does that align with your cash flow?",
                botSpanish = "Podríamos ajustar a $10,800, pero únicamente con pago anual anticipado. ¿Cómo encaja eso con su flujo de caja?",
                panic = PanicSuggestion(
                    userSpanish = "Pagar todo por adelantado es difícil; ¿podríamos pagar trimestralmente?",
                    translatedEnglish = "Paying all upfront is hard; could we pay quarterly?",
                    conservative = "Upfront payment is difficult for our budgeting. Could we structure this quarterly?",
                    natural = "Paying upfront is tough for us — would quarterly billing work instead?",
                    advanced = "Full upfront payment strains our working capital; quarterly billing at that price lets us finalize today."
                )
            ),
            DialogTurn(
                botEnglish = "Quarterly billing is workable at $11,200 including priority SLA support. That is our floor. Where do you land?",
                botSpanish = "Facturación trimestral es viable en $11,200 con soporte prioritario SLA. Es nuestro piso. ¿Dónde nos ubicamos?",
                panic = PanicSuggestion(
                    userSpanish = "Cerremos en $11,000 con el soporte prioritario incluido.",
                    translatedEnglish = "Let's close at $11,000 with priority support included.",
                    conservative = "Could we settle at $11,000 flat with priority support included in the agreement?",
                    natural = "Let's meet in the middle at $11,000 with priority support, and we have a deal.",
                    advanced = "If you can lock in $11,000 with priority SLA included, I can approve and sign the contract this week."
                )
            )
        ),
        closingEnglish = "That sounds like a win-win partnership. I'll forward the revised agreement to legal right away. Pleasure doing business!",
        closingSpanish = "Suena como una sociedad beneficiosa para ambos. Enviaré el contrato revisado a legal de inmediato. ¡Un placer!"
    )

    val debate = PracticeScenario(
        id = "debate",
        title = "Debates",
        level = ScenarioLevel.ADVANCED,
        durationMinutes = 12,
        category = "Persuasion",
        persona = Persona(
            id = "dana",
            name = "Dana Whitfield",
            firstName = "Dana",
            role = "Debate Moderator · Remote Work Policy",
            accent = "US English · Neutral & Articulate"
        ),
        targetKeywords = listOf("evidence", "concede", "counterpoint", "premise"),
        extraKeywords = listOf("rebuttal", "productivity", "synergy"),
        vocabulary = listOf(
            VocabWord("counterpoint", "contraargumento estructurado"),
            VocabWord("concede", "conceder o admitir un punto válido"),
            VocabWord("premise", "premisa fundamental de una tesis"),
            VocabWord("rebuttal", "refutación lógica"),
            VocabWord("evidence", "evidencia empírica")
        ),
        idioms = listOf(
            NativeIdiom("Play devil's advocate", "Argue the opposite side to test how solid the argument really is."),
            NativeIdiom("Fair point", "Acknowledge an opponent's valid argument with poise.")
        ),
        turns = listOf(
            DialogTurn(
                botEnglish = "Today's motion: remote-first work should be the default across knowledge workers. Do you support or oppose it?",
                botSpanish = "Moción de hoy: el trabajo remoto debe ser el estándar para trabajadores del conocimiento. ¿Apoyas o te opones?",
                panic = PanicSuggestion(
                    userSpanish = "Apoyo la moción porque aumenta la productividad y reduce el estrés del tráfico.",
                    translatedEnglish = "I support the motion because it boosts productivity and reduces commute stress.",
                    conservative = "I support the motion because it increases productivity and enhances employee well-being.",
                    natural = "I'm in favor — people get more done with fewer interruptions and zero commute time.",
                    advanced = "I support the motion: autonomy consistently maximizes deep-work productivity and broadens the talent pool."
                )
            ),
            DialogTurn(
                botEnglish = "Critics argue remote teams lose serendipitous creativity and organic mentoring. How would you counter that claim?",
                botSpanish = "Los críticos afirman que los equipos remotos pierden creatividad espontánea y mentoría orgánica. ¿Cómo refutas eso?",
                panic = PanicSuggestion(
                    userSpanish = "Con buenas herramientas y reuniones deliberadas, la creatividad se mantiene o incluso mejora.",
                    translatedEnglish = "With good tools and deliberate meetings, creativity holds up or improves.",
                    conservative = "With structured communication tools, creativity and mentoring can be actively fostered.",
                    natural = "Creativity depends on psychological safety and team clarity, not sharing the same physical room.",
                    advanced = "Creativity thrives on psychological safety and intentional collaboration, which are organizational choices rather than office amenities."
                )
            ),
            DialogTurn(
                botEnglish = "For your closing argument: what is the single strongest point your opposition could raise, and how do you concede or rebut it?",
                botSpanish = "Para su argumento de cierre: ¿cuál es el punto más fuerte que la oposición podría presentar, y cómo lo concede o refuta?",
                panic = PanicSuggestion(
                    userSpanish = "Que se debilita la conexión humana, pero se compensa con encuentros presenciales trimestrales.",
                    translatedEnglish = "That human connection weakens, but it's compensated by quarterly in-person gatherings.",
                    conservative = "They may point to weaker team cohesion, but structured off-sites effectively bridge that gap.",
                    natural = "They'd say we risk team isolation — that's a fair concern, but quarterly meetups solve it.",
                    advanced = "The opposition rightly notes social erosion; I concede that challenge, yet deliberate off-sites generate higher relational value than obligatory presence."
                )
            )
        ),
        closingEnglish = "Compelling arguments delivered with poise and precision. Thank you for an engaging and rigorous debate!",
        closingSpanish = "Argumentos persuasivos presentados con aplomo y precisión. ¡Gracias por un debate tan riguroso!"
    )

    val allScenarios = listOf(coffee, tickets, interview, hotel, negotiation, debate)

    fun findById(id: String): PracticeScenario {
        return allScenarios.find { it.id == id } ?: interview
    }
}
