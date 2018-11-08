package itc.ink.explorefuture_android.mine.settings.mode;

/**
 * Created by yangwenjiang on 2018/11/8.
 */

public class ReceiveAddressDataMode {
    private String name="";
    private String phone_num="";
    private String receive_address="";
    private String def_address="";

    public ReceiveAddressDataMode() {
    }

    public ReceiveAddressDataMode(String name, String phone_num, String receive_address, String def_address) {
        this.name = name;
        this.phone_num = phone_num;
        this.receive_address = receive_address;
        this.def_address = def_address;
    }

    @Override
    public String toString() {
        return "ReceiveAddressDataMode{" +
                "name='" + name + '\'' +
                ", phone_num='" + phone_num + '\'' +
                ", receive_address='" + receive_address + '\'' +
                ", def_address='" + def_address + '\'' +
                '}';
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone_num() {
        return phone_num;
    }

    public void setPhone_num(String phone_num) {
        this.phone_num = phone_num;
    }

    public String getReceive_address() {
        return receive_address;
    }

    public void setReceive_address(String receive_address) {
        this.receive_address = receive_address;
    }

    public String getDef_address() {
        return def_address;
    }

    public void setDef_address(String def_address) {
        this.def_address = def_address;
    }
}
