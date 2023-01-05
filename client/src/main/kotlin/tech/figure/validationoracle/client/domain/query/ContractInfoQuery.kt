package tech.figure.validationoracle.client.domain.query

import tech.figure.validationoracle.client.domain.query.base.EmptyQueryBody

object ContractInfoQuery : EmptyQueryBody() {
    override val queryDescription: String = "query for contract metadata"
}
