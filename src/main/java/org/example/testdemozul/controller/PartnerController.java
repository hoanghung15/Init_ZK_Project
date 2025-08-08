package org.example.testdemozul.controller;

import com.google.api.client.util.DateTime;
import org.example.testdemozul.dao.PartnerDAO;
import org.example.testdemozul.model.Partner;
import org.example.testdemozul.model.Task;
import org.w3c.dom.Text;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Button;
import org.zkoss.zul.Cell;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;

import java.util.ArrayList;
import java.util.List;

public class PartnerController extends SelectorComposer<Component> {
    @Wire
    private Combobox cbStatus;

    @Wire
    private Textbox tbPartnerName, tbPartnerAddress, tbMST, tbPartnerID, tbDescription;

    @Wire
    private Button btnSave, btnAccept, btnRefresh, btnDelete;

    @Wire
    private Listbox lbPartner;

    private PartnerDAO partnerDAO = new PartnerDAO();
    private List<Partner> partnerList = new ArrayList<>();
    private Integer tmpPartnerID = null;

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);

        partnerList = partnerDAO.getAllPartners();
        applyListPartner();

        btnSave.addEventListener(Events.ON_CLICK, event -> {
            if (tmpPartnerID == null) {
                createNewPartner();
            } else {
                Messagebox.show("Update");
            }
        });
        btnRefresh.addEventListener(Events.ON_CLICK, event -> {
            Executions.sendRedirect(null);
        });
        btnDelete.addEventListener(Events.ON_CLICK, event -> {
            if (tmpPartnerID != null) {
                Messagebox.show("Bạn có muốn xóa thông tin dối tác không?", "Thông báo",
                        Messagebox.OK, Messagebox.INFORMATION,
                        clickEvent -> {
                            if (clickEvent.getName().equals("onOK")) {
                                partnerDAO.deletePartner(tmpPartnerID);
                                Executions.sendRedirect(null);
                            }
                        });
                partnerDAO.deletePartner(tmpPartnerID);
            } else {
                Messagebox.show("Bạn phải chọn đối tác để thao tác ");
            }
        });

        lbPartner.addEventListener(Events.ON_SELECT, event -> {
            if (lbPartner.getSelectedItem() != null) {
                Partner partner = lbPartner.getSelectedItem().getValue();
                tmpPartnerID = partner.getId();
                fillForm(partner);
            }
        });
    }

    /**
     * Create new Partner
     */
    public void createNewPartner() {
        if (tbPartnerID.getValue() == null || tbPartnerID.getValue().equals("")
                || tbMST.getValue() == null || tbMST.getValue().equals("")
                || tbPartnerName.getValue() == null || tbPartnerName.getValue().equals("")
                || tbPartnerAddress.getValue() == null || tbPartnerAddress.getValue().equals("")) {
            Messagebox.show("Vui lòng nhập đầu đủ thông tin đối tác !");
        } else {
            if (partnerDAO.getPartnerByMST(tbMST.getValue()).getMst() != null) {
                Messagebox.show("Đã tồn tại đối tác với Mã số thuế này !");
            } else {
                Partner partner = new Partner();
                partner.setPartner_id(tbPartnerID.getValue());
                partner.setMst(tbMST.getValue());
                partner.setName(tbPartnerName.getValue());
                partner.setAddress(tbPartnerAddress.getValue());
                if (cbStatus.getValue() != null && !cbStatus.getValue().equals("")) {
                    partner.setStatus(cbStatus.getSelectedItem().getValue());
                } else {
                    partner.setStatus("PENDING");
                }
                partner.setTimestamp(new DateTime(System.currentTimeMillis()));
                partner.setDescription(tbDescription.getValue());
                partnerDAO.createNewPartner(partner);
                Messagebox.show("Đã tạo đối tác thành công!", "Thông báo",
                        Messagebox.OK, Messagebox.INFORMATION,
                        clickEvent -> {
                            if (clickEvent.getName().equals("onOK")) {
                                Executions.sendRedirect(null);
                            }
                        });
            }
        }

    }

    /**
     * Get all Partner
     */
    public void applyListPartner() {
        ListModelList<Partner> model = new ListModelList<>(partnerList);
        lbPartner.setModel(model);

        lbPartner.setItemRenderer((Listitem item, Partner partner, int index) -> {
            item.setValue(partner);
            new Listcell(String.valueOf(index + 1)).setParent(item);
            new Listcell(partner.getPartner_id()).setParent(item);
            new Listcell(partner.getName()).setParent(item);
            new Listcell(partner.getMst()).setParent(item);
            new Listcell(partner.getAddress()).setParent(item);
            new Listcell(partner.getStatus()).setParent(item);
            new Listcell(partner.getTimestamp().toString()).setParent(item);
        });
    }

    /**
     * Fill Form
     */
    public void fillForm(Partner partner) {
        tbPartnerID.setValue(partner.getPartner_id());
        tbMST.setValue(partner.getMst());
        tbPartnerName.setValue(partner.getName());
        tbPartnerAddress.setValue(partner.getAddress());
        selectComboboxByValue(cbStatus, partner.getStatus());
        tbDescription.setValue(partner.getDescription());


    }

    private void selectComboboxByValue(Combobox combobox, Object value) {
        for (Comboitem item : combobox.getItems()) {
            if (item.getValue().equals(value)) {
                combobox.setSelectedItem(item);
                return;
            }
        }
        // Nếu không tìm thấy, bỏ chọn
        combobox.setSelectedItem(null);
    }

}
