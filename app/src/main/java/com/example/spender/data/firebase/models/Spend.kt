package com.example.spender.data.firebase.models

import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.GeoPoint

data class Spend(
    val name: String,
    val category: String,
    val amount: Double,
    val geoPoint: GeoPoint,
    val members: List<Pair<Friend, MemberActivity>>,
    val docRef: DocumentReference
)

data class MemberActivity(
    val payment: Double,
    val debt: List<Pair<Friend, Double>>
)

/*
N - общее количество человек разделяющий трату
1) n человек (<= N) платят за всех, N - n (>= 0) человек ничего им не должны или возвращают долг

    1.1) n = N или n = 0 -> Крайний случай - каждый платит сам за себя:
        1.1.1) к каждому человеку привязана конкретная сумма платежа (сумма платежей == сумме траты)
        1.1.2) трата делится поровну - сумма платежа каждого человека == Общая сумма / N

    1.2) n = [1..(N - 1)] -> n человек платят за всех

        1.2.1) n человек - привязана конкретная сумма траты (сумма платежей == сумме траты)
               N - n человек - привязан флаг отсутствия долга (debtToUsersList = null)
               N - n человек - привязана конкретная сумма долга debtToUsersList = [User: debt, ...]
               N - n человек - привязана сумма долга = "Пропорциональное распределение долгов"
                    debtToUsersList = [Pair(User, debt), ...]:
                        forEach {
                            it.debt = it.User1.payment / (N - n)
                        }

        1.2.2) n человек - привязана сумма траты = Общая сумма / n
               N - n человек - привязан флаг отсутствия долга (debtToUsersList = null)
               N - n человек - привязана конкретная сумма долга debtToUsersList = [User: debt, ...]
               N - n человек - привязана сумма долга = "Пропорциональное распределение долгов"
                    debtToUsersList = [Pair(User, debt), ...]:
                        forEach {
                            it.debt = it.User1.payment / (N - n)
                        }
*/
