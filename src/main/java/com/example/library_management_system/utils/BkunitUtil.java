package com.example.library_management_system.utils;

/**
 * @ author Captain
 * @ date 2018/9/27
 * @ description as bellow.
 */


public class BkunitUtil
{
    // status
    public final static int
            BORROWED = 1,
            NORMAL = 2,
            RESERVATION = 3;

    // damageStatus
    public final static int
            LOST = 4,
            MILD_DAMAGE = 5,        // 轻度
            MODERATE_DAMAGE = 6,    // 中度
            MAJOR_DAMAGE = 7,       // 重度
            NO_DAMAGE = 8;
}
