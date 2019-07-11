package com.aihtd.translatelanguage.bean;

import java.util.List;

/**
 * 所在包名：com.aihtd.translatelanguage.bean
 * 描述：
 * 作者：Dabin
 * 创建时间：2019/7/5
 * 修改人：
 * 修改时间：
 * 修改描述：
 */
public class AsrResultBean {


    /**
     * results_recognition : ["你好你好你好你好"]
     * result_type : final_result
     * best_result : 你好你好你好你好
     * origin_result : {"corpus_no":6710087297781873057,"err_no":0,"result":{"word":["你好你好你好你好"]},"sn":"02ae585e-3433-4557-962f-4e2419bfff10","voice_energy":24355.83203125}
     * error : 0
     */

    private String result_type;
    private String best_result;
    private OriginResultBean origin_result;
    private int error;
    private List<String> results_recognition;

    public String getResult_type() {
        return result_type;
    }

    public void setResult_type(String result_type) {
        this.result_type = result_type;
    }

    public String getBest_result() {
        return best_result;
    }

    public void setBest_result(String best_result) {
        this.best_result = best_result;
    }

    public OriginResultBean getOrigin_result() {
        return origin_result;
    }

    public void setOrigin_result(OriginResultBean origin_result) {
        this.origin_result = origin_result;
    }

    public int getError() {
        return error;
    }

    public void setError(int error) {
        this.error = error;
    }

    public List<String> getResults_recognition() {
        return results_recognition;
    }

    public void setResults_recognition(List<String> results_recognition) {
        this.results_recognition = results_recognition;
    }

    public static class OriginResultBean {
        /**
         * corpus_no : 6710087297781873057
         * err_no : 0
         * result : {"word":["你好你好你好你好"]}
         * sn : 02ae585e-3433-4557-962f-4e2419bfff10
         * voice_energy : 24355.83203125
         */

        private long corpus_no;
        private int err_no;
        private ResultBean result;
        private String sn;
        private double voice_energy;

        public long getCorpus_no() {
            return corpus_no;
        }

        public void setCorpus_no(long corpus_no) {
            this.corpus_no = corpus_no;
        }

        public int getErr_no() {
            return err_no;
        }

        public void setErr_no(int err_no) {
            this.err_no = err_no;
        }

        public ResultBean getResult() {
            return result;
        }

        public void setResult(ResultBean result) {
            this.result = result;
        }

        public String getSn() {
            return sn;
        }

        public void setSn(String sn) {
            this.sn = sn;
        }

        public double getVoice_energy() {
            return voice_energy;
        }

        public void setVoice_energy(double voice_energy) {
            this.voice_energy = voice_energy;
        }

        public static class ResultBean {
            private List<String> word;

            public List<String> getWord() {
                return word;
            }

            public void setWord(List<String> word) {
                this.word = word;
            }
        }
    }
}
