package com.serkomma.musicmap.biz.exceptions

import com.serkomma.musicmap.common.models.MusicWorkMode

class DbNotConfiguredException(val workMode: MusicWorkMode): Exception(
    "Database is not configured properly for work mode $workMode"
)