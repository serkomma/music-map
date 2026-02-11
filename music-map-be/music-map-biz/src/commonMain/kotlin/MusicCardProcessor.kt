package com.serkomma.musicmap.biz

import com.serkomma.musicmap.common.MusicContext
import com.serkomma.musicmap.common.MusicCorSettings
import com.serkomma.musicmap.common.models.MusicState

class MusicCardProcessor(val coreSettings: MusicCorSettings){
    suspend fun exec(ctx: MusicContext) {
        ctx.cardResponse = CardStub.get()
//        ctx.cardsResponse = CardStub.prepareSearchList("ad search", MkplDealSide.DEMAND).toMutableList()
        ctx.state = MusicState.RUNNING
    }
}
