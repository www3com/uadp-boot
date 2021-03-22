package com.upbos.data.mapper;

public enum InvokeMethodEnum {
    Insert(new InvokeInsertMethodStrategy()),
    Update(new InvokeUpdateMethodStrategy() ),
    Delete(new InvokeDeleteMethodStrategy()),
    BatchDelete(new InvokeBatchDeleteMethodStrategy()),
    Select(new InvokeSelectMethodStrategy()),
    SelectOne(new InvokeSelectOneMethodStrategy());

    private InvokeMethodStrategy invokeMethodStrategy;

    InvokeMethodEnum(InvokeMethodStrategy invokeMethodStrategy) {
        this.invokeMethodStrategy = invokeMethodStrategy;
    }

    public InvokeMethodStrategy getInvokeMethodStrategy() {
        return invokeMethodStrategy;
    }
}
