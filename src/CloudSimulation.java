import org.cloudbus.cloudsim.*;
import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.provisioners.BwProvisionerSimple;
import org.cloudbus.cloudsim.provisioners.PeProvisionerSimple;
import org.cloudbus.cloudsim.provisioners.RamProvisionerSimple;

import java.util.*;

public class CloudSimulation {

    public static void simulate(int numberOfResumes) {
        try {
            int numUser = 1;
            Calendar calendar = Calendar.getInstance();
            boolean traceFlag = false;

            CloudSim.init(numUser, calendar, traceFlag);

            Datacenter datacenter = createDatacenter("Datacenter_1");

            DatacenterBroker broker = new DatacenterBroker("Broker");
            int brokerId = broker.getId();

            List<Vm> vmList = new ArrayList<>();

            for (int i = 0; i < numberOfResumes; i++) {
                Vm vm = new Vm(i, brokerId, 1000, 1, 512, 1000, 10000, "Xen", new CloudletSchedulerTimeShared());
                vmList.add(vm);
            }

            broker.submitVmList(vmList);

            List<Cloudlet> cloudletList = new ArrayList<>();

            for (int i = 0; i < numberOfResumes; i++) {
                Cloudlet cloudlet = new Cloudlet(i, 40000, 1, 300, 300,
                        new UtilizationModelFull(),
                        new UtilizationModelFull(),
                        new UtilizationModelFull());

                cloudlet.setUserId(brokerId);
                cloudletList.add(cloudlet);
            }

            broker.submitCloudletList(cloudletList);

            CloudSim.startSimulation();
            CloudSim.stopSimulation();

            List<Cloudlet> resultList = broker.getCloudletReceivedList();

            System.out.println("\nCloud Simulation Results:");
            for (Cloudlet cloudlet : resultList) {
                System.out.println("Resume Task " + cloudlet.getCloudletId()
                        + " processed in VM " + cloudlet.getVmId());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static Datacenter createDatacenter(String name) {

        List<Host> hostList = new ArrayList<>();

        List<Pe> peList = new ArrayList<>();
        peList.add(new Pe(0, new PeProvisionerSimple(1000)));

        hostList.add(new Host(
                0,
                new RamProvisionerSimple(2048),
                new BwProvisionerSimple(10000),
                1000000,
                peList,
                new VmSchedulerTimeShared(peList)
        ));

        String arch = "x86";
        String os = "Linux";
        String vmm = "Xen";

        DatacenterCharacteristics characteristics = new DatacenterCharacteristics(
                arch, os, vmm, hostList, 10.0, 3.0, 0.05, 0.1, 0.1
        );

        try {
            return new Datacenter(name, characteristics,
                    new VmAllocationPolicySimple(hostList), new LinkedList<>(), 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
