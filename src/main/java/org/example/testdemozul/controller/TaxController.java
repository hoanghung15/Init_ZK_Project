package org.example.testdemozul.controller;

import org.example.testdemozul.dao.ContractDAO;
import org.example.testdemozul.dao.PartnerDAO;
import org.example.testdemozul.dao.TaxDAO;
import org.example.testdemozul.model.Contract;
import org.example.testdemozul.model.Partner;
import org.example.testdemozul.model.Tax;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TaxController extends SelectorComposer<Component> {
    @Wire
    private Combobox cbPartner, cbContract, cbTypeService, cbTypeTax;

    @Wire
    private Button btnCancel, btnSave, btnRefresh;

    @Wire
    private Datebox dbEndDate, dbStartDate;

    @Wire
    private Listbox lbTax;

    private PartnerDAO partnerDAO = new PartnerDAO();
    private ContractDAO contractDAO = new ContractDAO();
    private TaxDAO taxDAO = new TaxDAO();

    List<Partner> partnerList = new ArrayList<>();
    List<Contract> contractList = new ArrayList<>();
    List<Tax> taxList = new ArrayList<>();

    private Integer tmpTax = null;

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);

        taxList = taxDAO.getAllTaxes();

        applyListBoxTax();
        applyCbPartner();
        applyCBContract();

        cbTypeService.addEventListener(Events.ON_SELECT, event -> {
            contractList = contractDAO.getAllContractWithFilter(null, null, null, null, cbTypeService.getSelectedItem().getValue());
            cbContract.getItems().clear();
            for (Contract contract : contractList) {
                Comboitem comboitem = new Comboitem(contract.getName() + " - " + contract.getNumberContract());
                comboitem.setValue(contract.getId());
                comboitem.setParent(cbContract);
            }
            System.out.println(contractList.size());
        });

        cbContract.addEventListener(Events.ON_SELECT, event -> {
            Contract tmp = new Contract();
            tmp = cbContract.getSelectedItem().getValue();
            selectComboboxByValue(cbTypeService, tmp.getContractType());
        });

        btnRefresh.addEventListener(Events.ON_CLICK, event -> {
            Executions.sendRedirect(null);
        });

        btnSave.addEventListener(Events.ON_CLICK, event -> {
            createNewTax();
            Executions.sendRedirect(null);
        });

        btnCancel.addEventListener(Events.ON_CLICK, event -> {
            Executions.sendRedirect(null);
        });
    }

    public void applyCbPartner() {
        partnerList = partnerDAO.getPartnerByAccepted();
        cbPartner.getItems().clear();
        for (Partner partner : partnerList) {
            Comboitem comboitem = new Comboitem(partner.getName());
            comboitem.setValue(partner.getId());
            comboitem.setParent(cbPartner);
        }
    }

    public void applyCBContract() {
        contractList = contractDAO.getAllContractWithFilter(null, null, null, null, null);
        cbContract.getItems().clear();
        for (Contract contract : contractList) {
            Comboitem comboitem = new Comboitem(contract.getName() + " - " + contract.getNumberContract());
            comboitem.setValue(contract);
            comboitem.setParent(cbContract);
        }
    }

    public void applyListBoxTax() {
        ListModelList<Tax> model = new ListModelList<>(taxList);
        lbTax.setModel(model);

        lbTax.setItemRenderer((Listitem item, Tax tax, int index) -> {
            item.setValue(tax);
            Contract contract = contractDAO.getContractById(tax.getContract_id());
            Partner partner = partnerDAO.getPartnerById(tax.getPartner_id());

            new Listcell(String.valueOf(index + 1)).setParent(item);
            new Listcell(contract.getContractType()).setParent(item);
            new Listcell(contract.getName()).setParent(item);
            new Listcell(partner.getName()).setParent(item);
            new Listcell(tax.getType()).setParent(item);
            if (tax.getType().equals("Không tính thuế")) {
                new Listcell("0%").setParent(item);
                tmpTax = 0;
            } else if (tax.getType().equals("Có tính thuế") && contract.getContractType().equals("Hợp đồng dịch vụ")) {
                new Listcell("1%").setParent(item);
                tmpTax = 1;
            } else if (tax.getType().equals("Có tính thuế") && contract.getContractType().equals("Hợp đồng mua bán")) {
                new Listcell("5%").setParent(item);
                tmpTax = 5;
            }
            new Listcell("ROUND(" + String.valueOf(tmpTax) + "*1.1,0) + " + tmpTax + "*10").setParent(item);

            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            new Listcell(tax.getStartDate() != null ? sdf.format(tax.getStartDate()) : "").setParent(item);
            new Listcell(tax.getEndDate() != null ? sdf.format(tax.getEndDate()) : "").setParent(item);
        });

    }

    public void createNewTax() {
        Tax tax = new Tax();
        Contract contract = cbContract.getSelectedItem().getValue();

        tax.setContract_id(contract.getId());
        tax.setPartner_id(cbPartner.getSelectedItem().getValue());

        tax.setStartDate(dbStartDate.getValue());
        tax.setEndDate(dbEndDate.getValue());

        tax.setType(cbTypeTax.getSelectedItem().getValue());

        taxDAO.createNewTax(tax);
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
