package com.xxl.job.core.handler;

/**
 * job handler
 *
 * @author liyh
 */
public abstract class IJobHandler {

    /**
     * execute handler, invoked when executor receives a scheduling request
     *
     * @throws Exception
     */
    public abstract void execute() throws Exception;

    /**
     * init handler, invoked when JobThread init
     */
    public void init() throws Exception {

    }

    /**
     * destroy handler, invoked when JobThread destroy
     */
    public void destroy() throws Exception {

    }


}
