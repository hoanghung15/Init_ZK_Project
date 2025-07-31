package org.example.testdemozul.controller;

import org.example.testdemozul.dao.ContractDAO;
import org.example.testdemozul.model.Contract;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;

import java.util.List;


public class ContractController extends SelectorComposer<Component> {
    @Wire
    private Label txtContractDone;

    @Wire
    private Label txtContractExDone;

    @Wire
    private Label txtContractPending;

    @Wire
    private Label txtContractExPending;

    @Wire
    private Label txtContractTotal;

    @Wire
    private Label txtContractExTotal;

    @Wire
    private Label txtContractDoneByManager;

    @Wire
    private Label txtContractExDoneByManager;

    @Wire
    private Label txtContractDoneByStaff;

    @Wire
    private Label txtContractExDoneByStaff;

    private ContractDAO contractDAO = new ContractDAO();

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);

        int contractDone = contractDAO.getAllContractsWithScopeAndStatus("Nội bộ", "DONE").size();
        int totalContractInternal = contractDAO.getAllContractsWithScopeAndStatus("Nội bộ", null).size();
        int contractPending = totalContractInternal - contractDone;
        int contractDoneByManager = contractDAO.getAllContractsWithRoleScopeStatus("Trưởng phòng", "Nội bộ", "DONE").size();
        int contractDoneByStaff = contractDAO.getAllContractsWithRoleScopeStatus("Nhân viên kinh doanh", "Nội bộ", "DONE").size();

        int contractExDone = contractDAO.getAllContractsWithScopeAndStatus("Đối tác ngoài", "DONE").size();
        int totalContractEx = contractDAO.getAllContractsWithScopeAndStatus("Đối tác ngoài", null).size();
        int contractExPending = totalContractEx - contractExDone;
        int contractExDoneByManager = contractDAO.getAllContractsWithRoleScopeStatus("Trưởng phòng", "Đối tác ngoài", "DONE").size();
        int contractExDoneByStaff = contractDAO.getAllContractsWithRoleScopeStatus("Nhân viên kinh doanh", "Đối tác ngoài", "DONE").size();

        txtContractDoneByStaff.setValue(String.valueOf(contractDoneByStaff));
        txtContractDoneByManager.setValue(String.valueOf(contractDoneByManager));
        txtContractDone.setValue(String.valueOf(contractDone));
        txtContractTotal.setValue(String.valueOf(totalContractInternal));
        txtContractPending.setValue(String.valueOf(contractPending));

//ex
        txtContractExDoneByStaff.setValue(String.valueOf(contractExDoneByStaff));
        txtContractExDoneByManager.setValue(String.valueOf(contractExDoneByManager));
        txtContractExDone.setValue(String.valueOf(contractExDone));
        txtContractExTotal.setValue(String.valueOf(totalContractEx));
        txtContractExPending.setValue(String.valueOf(contractExPending));

    }
}
