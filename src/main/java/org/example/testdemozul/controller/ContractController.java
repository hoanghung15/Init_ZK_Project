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

        int contractDone = contractDAO.getAllContractWithFilter(null,"DONE", null, "Nội bộ",null).size() ;
        int totalContractInternal = contractDAO.getAllContractWithFilter(null,null, null, "Nội bộ",null).size() ;
        System.out.println(totalContractInternal);
        int contractPending = totalContractInternal - contractDone;
        int contractDoneByManager = contractDAO.getAllContractWithFilter(null,"DONE", "Trưởng phòng", "Nội bộ",null).size() ;
        int contractDoneByStaff = contractDAO.getAllContractWithFilter(null,"DONE", "Nhân viên kinh doanh", "Nội bộ",null).size() ;

        int contractExDone = contractDAO.getAllContractWithFilter(null,"DONE", null, "Đối tác ngoài",null).size() ;
        int totalContractEx = contractDAO.getAllContractWithFilter(null,null, null, "Đối tác ngoài",null).size() ;
        int contractExPending = totalContractEx - contractExDone;
        int contractExDoneByManager = contractDAO.getAllContractWithFilter(null,"DONE", "Trưởng phòng", "Đối tác ngoài",null).size() ;
        int contractExDoneByStaff =  contractDAO.getAllContractWithFilter(null,"DONE", "Nhân viên kinh doanh", "Đối tác ngoài",null).size() ;

        txtContractDoneByStaff.setValue(String.valueOf(contractDoneByStaff));
        txtContractDoneByManager.setValue(String.valueOf(contractDoneByManager));
        txtContractDone.setValue(String.valueOf(contractDone));
        txtContractTotal.setValue(String.valueOf(totalContractInternal));
        txtContractPending.setValue(String.valueOf(contractPending));

        txtContractExDoneByStaff.setValue(String.valueOf(contractExDoneByStaff));
        txtContractExDoneByManager.setValue(String.valueOf(contractExDoneByManager));
        txtContractExDone.setValue(String.valueOf(contractExDone));
        txtContractExTotal.setValue(String.valueOf(totalContractEx));
        txtContractExPending.setValue(String.valueOf(contractExPending));

    }
}
