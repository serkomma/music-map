package com.serkomma.musicmap.repo.postgres

import com.serkomma.musicmap.common.models.MusicCard
import com.serkomma.musicmap.common.repo.DBOneResponseOk
import com.serkomma.musicmap.common.repo.DbManyResponseOk

fun MusicCard.pack() = DBOneResponseOk(this)
fun List<MusicCard>.pack() = DbManyResponseOk(this)