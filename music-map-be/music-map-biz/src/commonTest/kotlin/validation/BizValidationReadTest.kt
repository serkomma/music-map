package com.serkomma.musicmap.biz.validation

import com.serkomma.musicmap.common.models.MusicCommand
import kotlin.test.Test

class BizValidationReadTest: BaseBizValidationTest() {
    override val command = MusicCommand.READ

    @Test fun correctId() = validationIdCorrect(command, processor)
    @Test fun trimId() = validationIdTrim(command, processor)
    @Test fun emptyId() = validationIdEmpty(command, processor)

}
