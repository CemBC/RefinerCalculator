import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Main {
    private static int ELDE_KALAN_RAW;
    private static DefaultListModel<String> historyModel = new DefaultListModel<>();
    private static JLabel dynamicReturnRateLabel = new JLabel("Return Rate: %15");

    public static void main(String[] args) {

        JOptionPane.showMessageDialog(null,
                "-This program calculates the result with worst return rate situation-",
                "Attention",
                JOptionPane.WARNING_MESSAGE);

        JFrame frame = new JFrame("Refined Calculator");
        frame.setSize(600, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        JTabbedPane tabbedPane = new JTabbedPane();

        JPanel calculatorPanel = createCalculatorPanel();
        JPanel craftPanel = createCraftPanel();
        JPanel refineForStonePanel = createRefineForStone();
        JPanel neededRawForStonePnale =createNeededRawForStone();
        JPanel historyPanel = createHistoryPanel();

        tabbedPane.addTab("1-Needed Raw Calculator", calculatorPanel);
        tabbedPane.addTab("2-Refine Result Calculator", craftPanel);
        tabbedPane.addTab("3-Refine for Stone" , refineForStonePanel);
        tabbedPane.addTab("4-Needed Raw for Stone" , neededRawForStonePnale);
        tabbedPane.addTab("5-Refined Calculator History", historyPanel);

        frame.add(tabbedPane, BorderLayout.CENTER);
        frame.setVisible(true);
    }

    private static JPanel createCalculatorPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(8, 2, 10, 10));

        JLabel labelIstenilenDeger = new JLabel("Wanted Refined Value:");
        JTextField textIstenilenDeger = new JTextField();

        JLabel labelReturnRate = new JLabel("Return Rate (%):");
        JTextField textReturnRate = new JTextField("15");
        textReturnRate.setEditable(false);

        JLabel labelTier = new JLabel("Tier:");
        JComboBox<String> comboTier = new JComboBox<>(new String[]{"3", "4", "5", "6", "7", "8"});

        JCheckBox royalBuffCheckBox = new JCheckBox("Royal Buff");
        JCheckBox focusCheckBox = new JCheckBox("Focus");
        JCheckBox activity10CheckBox = new JCheckBox("%10 Activity");
        JCheckBox activity20CheckBox = new JCheckBox("%20 Activity");

        ActionListener checkBoxListener = e -> {
            int baseRate = 15;

            if (royalBuffCheckBox.isSelected() && focusCheckBox.isSelected()) {
                baseRate = 53;
            } else if (royalBuffCheckBox.isSelected()) {
                baseRate = 36;
            } else if (focusCheckBox.isSelected()) {
                baseRate = 43;
            }

            if (activity10CheckBox.isSelected()) {
                baseRate += 10;
            } else if (activity20CheckBox.isSelected()) {
                baseRate += 20;
            }

            textReturnRate.setText(String.valueOf(baseRate));
            dynamicReturnRateLabel.setText("Return Rate: %" + baseRate);
        };

        royalBuffCheckBox.addActionListener(checkBoxListener);
        focusCheckBox.addActionListener(checkBoxListener);
        activity10CheckBox.addActionListener(checkBoxListener);
        activity20CheckBox.addActionListener(checkBoxListener);

        JButton calculateButton = new JButton("Calculate");
        JLabel resultLabel = new JLabel("Result: ");


        calculateButton.addActionListener(e -> {
            try {
                int istenilenDeger = Integer.parseInt(textIstenilenDeger.getText());
                double returnRate = Double.parseDouble(textReturnRate.getText()) / 100.0;
                String selectedTier = (String) comboTier.getSelectedItem();

                int bolunecekSayi;
                    int tier = Integer.parseInt(selectedTier);
                    switch (tier) {
                        case 3:
                        case 4:
                            bolunecekSayi = 2;
                            break;
                        case 5:
                            bolunecekSayi = 3;
                            break;
                        case 6:
                            bolunecekSayi = 4;
                            break;
                        case 7:
                        case 8:
                            bolunecekSayi = 5;
                            break;
                        default:
                            throw new IllegalArgumentException("Invalid Tier Value!");
                    }


                int elimizdekiRaw = istenilenDeger * bolunecekSayi;
                int sonuc = 0;

                for (int i = elimizdekiRaw; i >= bolunecekSayi; i--) {
                    if (deneme(i, returnRate, bolunecekSayi) == istenilenDeger) {
                        sonuc = i;
                        break;
                    }
                }
                String sonucLabel = "Raw";
                resultLabel.setText("Result: Needed " + sonucLabel + " = " + sonuc);

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(panel, "Please enter valid value", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(panel, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        panel.add(labelIstenilenDeger);
        panel.add(textIstenilenDeger);
        panel.add(labelReturnRate);
        panel.add(textReturnRate);
        panel.add(labelTier);
        panel.add(comboTier);
        panel.add(royalBuffCheckBox);
        panel.add(focusCheckBox);
        panel.add(activity10CheckBox);
        panel.add(activity20CheckBox);
        panel.add(calculateButton);
        panel.add(resultLabel);

        return panel;
    }
    private static JPanel createRefineForStone() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(8, 2, 10, 10));

        JLabel labelMaterial = new JLabel("Raw Material Number for Craft");
        JTextField textMaterial = new JTextField();

        JLabel labelReturnRate = new JLabel("Return Rate (%):");
        JTextField textReturnRate = new JTextField("15");
        textReturnRate.setEditable(false);

        JLabel labelTier = new JLabel("Tier:");
        JComboBox<String> comboTier = new JComboBox<>(new String[]{"3" ,
                "4" , "4.1", "4.2" ,"4.3" ,
                "5" , "5.1", "5.2" , "5.3",
                "6" , "6.1" , "6.2" , "6.3",
                "7","7.1", "7.2" , "7.3",
                "8" , "8.1" , "8.2" , "8.3"});


        JCheckBox royalBuffCheck = new JCheckBox("Royal Buff");
        JCheckBox focusCheck = new JCheckBox("Focus");
        JCheckBox activity10Check = new JCheckBox("%10 Activity");
        JCheckBox activity20Check = new JCheckBox("%20 Activity");


        JButton calculateButton = new JButton("Calculate");
        JLabel resultLabel = new JLabel("Result: ");
        JLabel eldeKalanLabel = new JLabel("Remaining Raw: ");


        ActionListener updateReturnRate = e -> {
            int baseRate = 15;

            if (royalBuffCheck.isSelected() && focusCheck.isSelected()) {
                baseRate = 53;
            } else if (royalBuffCheck.isSelected()) {
                baseRate = 36;
            } else if (focusCheck.isSelected()) {
                baseRate = 43;
            }

            if (activity10Check.isSelected()) {
                baseRate += 10;
            } else if (activity20Check.isSelected()) {
                baseRate += 20;
            }

            textReturnRate.setText(String.valueOf(baseRate));
            dynamicReturnRateLabel.setText("Return Rate: %" + baseRate);
        };


        royalBuffCheck.addActionListener(updateReturnRate);
        focusCheck.addActionListener(updateReturnRate);
        activity10Check.addActionListener(updateReturnRate);
        activity20Check.addActionListener(updateReturnRate);

        calculateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    int material = Integer.parseInt(textMaterial.getText());
                    double returnRate = Double.parseDouble(textReturnRate.getText()) / 100.0;
                    String selectedTier = (String) comboTier.getSelectedItem();
                    int bolunecekSayi;
                    int çarpılacakSayi = 1;

                    switch (selectedTier) {
                        case "4.1":
                            bolunecekSayi = 2;
                            çarpılacakSayi = 2;
                            break;
                        case "5.1":
                            bolunecekSayi = 3;
                            çarpılacakSayi = 2;
                            break;
                        case "6.1":
                            bolunecekSayi = 4;
                            çarpılacakSayi = 2;
                            break;
                        case "7.1":
                        case "8.1":
                            bolunecekSayi = 5;
                            çarpılacakSayi = 2;
                            break;
                        case "4.2":
                            bolunecekSayi = 2;
                            çarpılacakSayi = 4;
                            break;
                        case "5.2":
                            bolunecekSayi = 3;
                            çarpılacakSayi = 4;
                            break;
                        case "6.2":
                            bolunecekSayi = 4;
                            çarpılacakSayi = 4;
                            break;
                        case "7.2":
                        case "8.2":
                            bolunecekSayi = 5;
                            çarpılacakSayi = 4;
                            break;
                        case "4.3":
                            bolunecekSayi = 2;
                            çarpılacakSayi = 8 ;
                            break;
                        case "5.3":
                            bolunecekSayi = 3;
                            çarpılacakSayi = 8;
                            break;
                        case "6.3":
                            bolunecekSayi = 4;
                            çarpılacakSayi = 8;
                            break;
                        case "7.3":
                        case "8.3":
                            bolunecekSayi = 5;
                            çarpılacakSayi = 8;
                            break;
                        case "3":
                        case "4":
                            bolunecekSayi = 2;
                            break;
                        case "5":
                            bolunecekSayi = 3;
                            break;
                        case "6":
                            bolunecekSayi = 4;
                            break;
                        case "7":
                        case "8":
                            bolunecekSayi = 5;
                            break;
                        default:
                            throw new IllegalArgumentException("Invalid Tier Value!");
                    }


                    int sonuc = deneme(material, returnRate, bolunecekSayi);
                    resultLabel.setText("Result: Result of refine = " + sonuc*çarpılacakSayi);
                    eldeKalanLabel.setText("Remaining raw: " + ELDE_KALAN_RAW);

                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:s");
                    String timestamp = LocalDateTime.now().format(formatter);
                    historyModel.addElement("3-> " +
                            "Material: " + material +
                            " / Tier: " + selectedTier +
                            " / Result: " + sonuc +
                            " / Remaining: " + ELDE_KALAN_RAW +
                            " / Return Rate: %" + (int) (returnRate * 100) +
                            "         ////" + timestamp);
                    historyModel.addElement("______________________________________________________________");
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(panel, "Please Enter Valid Value", "Error", JOptionPane.ERROR_MESSAGE);
                } catch (IllegalArgumentException ex) {
                    JOptionPane.showMessageDialog(panel, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });


        panel.add(labelMaterial);
        panel.add(textMaterial);
        panel.add(labelReturnRate);
        panel.add(textReturnRate);
        panel.add(labelTier);
        panel.add(comboTier);
        panel.add(royalBuffCheck);
        panel.add(focusCheck);
        panel.add(activity10Check);
        panel.add(activity20Check);
        panel.add(calculateButton);
        panel.add(resultLabel);
        panel.add(eldeKalanLabel);

        return panel;

    }
    private static JPanel createNeededRawForStone() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(8, 2, 10, 10));

        JLabel labelIstenilenDeger = new JLabel("Wanted Refined Value:");
        JTextField textIstenilenDeger = new JTextField();

        JLabel labelReturnRate = new JLabel("Return Rate (%):");
        JTextField textReturnRate = new JTextField("15");
        textReturnRate.setEditable(false);

        JLabel labelTier = new JLabel("Tier:");
        JComboBox<String> comboTier = new JComboBox<>(new String[]{"3" ,
                "4" , "4.1", "4.2" ,"4.3" ,
                "5" , "5.1", "5.2" , "5.3",
                "6" , "6.1" , "6.2" , "6.3",
                "7","7.1", "7.2" , "7.3",
                "8" , "8.1" , "8.2" , "8.3"});

        JCheckBox royalBuffCheckBox = new JCheckBox("Royal Buff");
        JCheckBox focusCheckBox = new JCheckBox("Focus");
        JCheckBox activity10CheckBox = new JCheckBox("%10 Activity");
        JCheckBox activity20CheckBox = new JCheckBox("%20 Activity");

        ActionListener checkBoxListener = e -> {
            int baseRate = 15;

            if (royalBuffCheckBox.isSelected() && focusCheckBox.isSelected()) {
                baseRate = 53;
            } else if (royalBuffCheckBox.isSelected()) {
                baseRate = 36;
            } else if (focusCheckBox.isSelected()) {
                baseRate = 43;
            }

            if (activity10CheckBox.isSelected()) {
                baseRate += 10;
            } else if (activity20CheckBox.isSelected()) {
                baseRate += 20;
            }

            textReturnRate.setText(String.valueOf(baseRate));
            dynamicReturnRateLabel.setText("Return Rate: %" + baseRate);
        };

        royalBuffCheckBox.addActionListener(checkBoxListener);
        focusCheckBox.addActionListener(checkBoxListener);
        activity10CheckBox.addActionListener(checkBoxListener);
        activity20CheckBox.addActionListener(checkBoxListener);

        JButton calculateButton = new JButton("Calculate");
        JLabel resultLabel = new JLabel("Result: ");


        calculateButton.addActionListener(e -> {
            try {
                int istenilenDeger = Integer.parseInt(textIstenilenDeger.getText());
                double returnRate = Double.parseDouble(textReturnRate.getText()) / 100.0;
                String selectedTier = (String) comboTier.getSelectedItem();

                int bolunecekSayi;
                int bölünecekSayı2 = 1;
                switch (selectedTier) {
                    case "4.1":
                        bolunecekSayi = 2;
                        bölünecekSayı2 = 2;
                        break;
                    case "5.1":
                        bolunecekSayi = 3;
                        bölünecekSayı2 = 2;
                        break;
                    case "6.1":
                        bolunecekSayi = 4;
                        bölünecekSayı2 = 2;
                        break;
                    case "7.1":
                    case "8.1":
                        bolunecekSayi = 5;
                        bölünecekSayı2 = 2;
                        break;
                    case "4.2":
                        bolunecekSayi = 2;
                        bölünecekSayı2 = 4;
                        break;
                    case "5.2":
                        bolunecekSayi = 3;
                        bölünecekSayı2 = 4;
                        break;
                    case "6.2":
                        bolunecekSayi = 4;
                        bölünecekSayı2 = 4;
                        break;
                    case "7.2":
                    case "8.2":
                        bolunecekSayi = 5;
                        bölünecekSayı2 = 4;
                        break;
                    case "4.3":
                        bolunecekSayi = 2;
                        bölünecekSayı2 = 8;
                        break;
                    case "5.3":
                        bolunecekSayi = 3;
                        bölünecekSayı2 = 8;
                        break;
                    case "6.3":
                        bolunecekSayi = 4;
                        bölünecekSayı2 = 8;
                        break;
                    case "7.3":
                    case "8.3":
                        bolunecekSayi = 5;
                        bölünecekSayı2 = 8;
                        break;
                    case "3":
                    case "4":
                        bolunecekSayi = 2;
                        break;
                    case "5":
                        bolunecekSayi = 3;
                        break;
                    case "6":
                        bolunecekSayi = 4;
                        break;
                    case "7":
                    case "8":
                        bolunecekSayi = 5;
                        break;
                    default:
                        throw new IllegalArgumentException("Invalid Tier Value!");
                }

                int elimizdekiRaw = istenilenDeger * bolunecekSayi;
                int sonuc = 0;

                for (int i = elimizdekiRaw; i >= bolunecekSayi; i--) {
                    if (deneme(i, returnRate, bolunecekSayi) == istenilenDeger) {
                        sonuc = i;
                        break;
                    }
                }
                String sonucLabel = "Raw";
                resultLabel.setText("Result: Needed " + sonucLabel + " = " + sonuc/bölünecekSayı2);

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(panel, "Please enter valid value", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(panel, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        panel.add(labelIstenilenDeger);
        panel.add(textIstenilenDeger);
        panel.add(labelReturnRate);
        panel.add(textReturnRate);
        panel.add(labelTier);
        panel.add(comboTier);
        panel.add(royalBuffCheckBox);
        panel.add(focusCheckBox);
        panel.add(activity10CheckBox);
        panel.add(activity20CheckBox);
        panel.add(calculateButton);
        panel.add(resultLabel);

        return panel;
    }

    private static JPanel createCraftPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(8, 2, 10, 10));

        JLabel labelMaterial = new JLabel("Raw Material Number for Craft");
        JTextField textMaterial = new JTextField();

        JLabel labelReturnRate = new JLabel("Return Rate (%):");
        JTextField textReturnRate = new JTextField("15");
        textReturnRate.setEditable(false);

        JLabel labelTier = new JLabel("Tier:");
        JComboBox<String> comboTier = new JComboBox<>(new String[]{"3", "4", "5", "6", "7", "8"});


        JCheckBox royalBuffCheck = new JCheckBox("Royal Buff");
        JCheckBox focusCheck = new JCheckBox("Focus");
        JCheckBox activity10Check = new JCheckBox("%10 Activity");
        JCheckBox activity20Check = new JCheckBox("%20 Activity");


        JButton calculateButton = new JButton("Calculate");
        JLabel resultLabel = new JLabel("Result: ");
        JLabel eldeKalanLabel = new JLabel("Remaining Raw: ");


        ActionListener updateReturnRate = e -> {
            int baseRate = 15;

            if (royalBuffCheck.isSelected() && focusCheck.isSelected()) {
                baseRate = 53;
            } else if (royalBuffCheck.isSelected()) {
                baseRate = 36;
            } else if (focusCheck.isSelected()) {
                baseRate = 43;
            }

            if (activity10Check.isSelected()) {
                baseRate += 10;
            } else if (activity20Check.isSelected()) {
                baseRate += 20;
            }

            textReturnRate.setText(String.valueOf(baseRate));
            dynamicReturnRateLabel.setText("Return Rate: %" + baseRate);
        };


        royalBuffCheck.addActionListener(updateReturnRate);
        focusCheck.addActionListener(updateReturnRate);
        activity10Check.addActionListener(updateReturnRate);
        activity20Check.addActionListener(updateReturnRate);

        calculateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    int material = Integer.parseInt(textMaterial.getText());
                    double returnRate = Double.parseDouble(textReturnRate.getText()) / 100.0;
                    String selectedTier = (String) comboTier.getSelectedItem();
                    int bolunecekSayi;
                    int tier = Integer.parseInt(selectedTier);

                    switch (tier) {
                        case 3:
                        case 4:
                            bolunecekSayi = 2;
                            break;
                        case 5:
                            bolunecekSayi = 3;
                            break;
                        case 6:
                            bolunecekSayi = 4;
                            break;
                        case 7:
                        case 8:
                            bolunecekSayi = 5;
                            break;
                        default:
                            throw new IllegalArgumentException("Invalid Tier Value!");
                    }


                    int sonuc = deneme(material, returnRate, bolunecekSayi);
                    resultLabel.setText("Result: Result of refine = " + sonuc);
                    eldeKalanLabel.setText("Remaining raw: " + ELDE_KALAN_RAW);

                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:s");
                    String timestamp = LocalDateTime.now().format(formatter);
                    historyModel.addElement("2-> " +
                            "Material: " + material +
                            " / Tier: " + selectedTier +
                            " / Result: " + sonuc +
                            " / Remaining: " + ELDE_KALAN_RAW +
                            " / Return Rate: %" + (int) (returnRate * 100) +
                            "         ////" + timestamp);
                    historyModel.addElement("______________________________________________________________");
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(panel, "Please Enter Valid Value", "Error", JOptionPane.ERROR_MESSAGE);
                } catch (IllegalArgumentException ex) {
                    JOptionPane.showMessageDialog(panel, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });


        panel.add(labelMaterial);
        panel.add(textMaterial);
        panel.add(labelReturnRate);
        panel.add(textReturnRate);
        panel.add(labelTier);
        panel.add(comboTier);
        panel.add(royalBuffCheck);
        panel.add(focusCheck);
        panel.add(activity10Check);
        panel.add(activity20Check);
        panel.add(calculateButton);
        panel.add(resultLabel);
        panel.add(eldeKalanLabel);

        return panel;
    }

    private static JPanel createHistoryPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        JList<String> historyList = new JList<>(historyModel);
        JScrollPane scrollPane = new JScrollPane(historyList);

        JButton clearButton = new JButton("Clear");
        clearButton.addActionListener(e -> historyModel.clear());

        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(clearButton, BorderLayout.SOUTH);

        return panel;
    }

    public static int deneme(int elimizdekiRaw, double returnRate, int bolunecekSayi) {
        if (elimizdekiRaw < bolunecekSayi) {
            ELDE_KALAN_RAW = elimizdekiRaw;
            return 0;
        }
        int sonuc1 = elimizdekiRaw / bolunecekSayi;
        double geriDonen = sonuc1 * bolunecekSayi * returnRate;
        geriDonen = (int) geriDonen;
        elimizdekiRaw = (int) (elimizdekiRaw - (sonuc1 * bolunecekSayi) + geriDonen);
        return sonuc1 + deneme(elimizdekiRaw, returnRate, bolunecekSayi);
    }
}
