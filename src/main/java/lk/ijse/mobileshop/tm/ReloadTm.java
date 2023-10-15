package lk.ijse.mobileshop.tm;

public class ReloadTm {
        private  String id;
        private  Double amount;
        private  String empid;


        public ReloadTm(String id, String empid, Double amount) {
            this.id = id;
            this.empid = empid;
            this.amount = amount;
        }

        public ReloadTm() {
        }

        public  String getId() {
            return id;
        }


        public void setId(String id) {
            this.id = id;
        }

        public  String getEmpid() {
            return empid;
        }

        public void setEmpid(String empid) {
            this.empid = empid;
        }

        public  Double getAmount() {
            return amount;
        }

        public void setAmount(Double amount) {
            this.amount = amount;
        }


}
