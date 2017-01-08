package com.learnings.myapps.azure;

import com.learnings.myapps.azure.Entity.Account;

import java.util.List;

/**
 * Created by Андрей on 26.12.2016.
 */

public interface DataFiller {
    void FillIn(List l, Account account);
}
