package com.github.rkbalgi.apps.proxyswitcher;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.io.File;

/**
 * A simple java application to switch between multiple proxy profiles on a windows pc (requires java8+)
 * The application show itself on the system tray and uses 'powershell' scripts to change proxy. The directory
 * containing the proxy-switcher.jar file should also contain the required powershell scripts with ps1 extension
 * (any number of powershell scripts can be present and each one will be shown as a menu item)
 *
 * @author Raghavendra Balgi (rkbalgi@gmail.com)
 * @date 23/02/2017
 */
public class ProxySwitcher {

    public static void main(String[] args) {


        if (SystemTray.isSupported()) {

            SystemTray systemTray = SystemTray.getSystemTray();
            String userDir = System.getProperty("user.dir");
            String[] ps1Files = new File(userDir).list((dir, name) -> {
                if (name.endsWith(".ps1") || name.endsWith(".PS1")) {
                    return true;
                }
                return false;
            });

            TrayIcon trayIcon = new TrayIcon(
                    new ImageIcon(ClassLoader.getSystemResource("com/github/rkbalgi/apps/proxyswitcher/ps.gif")).getImage());
            try {

                PopupMenu popupMenu = new PopupMenu();
                Menu proxiesMenu = new Menu("Proxies");

                java.util.List<CheckboxMenuItem> btnGrp = new java.util.ArrayList<>();

                for (final String ps1File : ps1Files) {
                    final CheckboxMenuItem item = new CheckboxMenuItem(ps1File, false);
                    item.addItemListener((e) -> {

                        if (e.getStateChange() == ItemEvent.SELECTED) {
                            System.out.println("applying .." + item.getLabel());
                            try {
                                String cmd = "cmd /c start powershell -ExecutionPolicy ByPass " + new File(userDir, ps1File).getAbsolutePath();
                                System.out.println(cmd);
                                Process process = Runtime.getRuntime().exec(cmd);
                                process.waitFor();
                                if (process.exitValue() == 0) {
                                    trayIcon.displayMessage("", "proxy settings changed to " + ps1File.substring(0, ps1File.indexOf('.')), TrayIcon.MessageType.INFO);
                                } else {
                                    trayIcon.displayMessage("", "proxy settings change failed. " + process.exitValue(), TrayIcon.MessageType.ERROR);
                                }
                            } catch (Exception e1) {
                                trayIcon.displayMessage("", "proxy settings change failed! " + e1.getMessage(), TrayIcon.MessageType.ERROR);

                            }


                            btnGrp.stream().filter(cItem -> cItem != item).forEach(cItem -> {
                                cItem.setState(false);

                            });

                        }

                    });
                    btnGrp.add(item);
                    proxiesMenu.add(item);


                }
                popupMenu.add(proxiesMenu);


                MenuItem exitMi = new MenuItem("Close");
                exitMi.addActionListener((e) -> System.exit(0));
                popupMenu.add(exitMi);
                trayIcon.setPopupMenu(popupMenu);


                systemTray.add(trayIcon);
                trayIcon.displayMessage("", "proxyswitcher is now running!", TrayIcon.MessageType.ERROR);
            } catch (AWTException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Error!");
                System.exit(1);
            }


        } else {
            SwingUtilities.invokeLater(() -> {
                JOptionPane.showMessageDialog(null, "System Tray is not supported on your OS");
                System.exit(1);
            });
        }
    }
}
