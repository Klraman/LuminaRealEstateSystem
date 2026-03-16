import MyLib.*;
import javax.swing.*;
import java.awt.*;

/**
 * Application shell. Owns the single JFrame window and swaps the active
 * JPanel when the user logs in or out.
 */
public class MainJFrame extends javax.swing.JFrame {

    private static final java.util.logging.Logger logger =
            java.util.logging.Logger.getLogger(MainJFrame.class.getName());

    private JPanel currentPage;

    public MainJFrame() {
        setTitle("Lumina Real Estate Sales & Management System");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        getContentPane().setLayout(new BorderLayout());
        showLogin();
    }

    // ── Navigation ──────────────────────────────────────────────────────

    public void showLogin() {
        switchTo(new LoginPage(this));
        setSize(500, 530);
        setLocationRelativeTo(null);
    }

    public void showAdminPage(Admin admin) {
        switchTo(new AdminPage(this, admin));
        setSize(1100, 700);
        setLocationRelativeTo(null);
    }

    public void showAgentPage(Agent agent) {
        switchTo(new AgentPage(this, agent));
        setSize(1100, 700);
        setLocationRelativeTo(null);
    }

    public void showBuyerPage(Buyer buyer) {
        switchTo(new BuyerPage(this, buyer));
        setSize(1050, 680);
        setLocationRelativeTo(null);
    }

    private void switchTo(JPanel panel) {
        if (currentPage != null) getContentPane().remove(currentPage);
        currentPage = panel;
        getContentPane().add(currentPage, BorderLayout.CENTER);
        revalidate();
        repaint();
    }

    // ── Entry point ─────────────────────────────────────────────────────

    public static void main(String[] args) {
        // Apply Nimbus L&F if available
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ReflectiveOperationException | UnsupportedLookAndFeelException ex) {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        }

        // Warm up the singleton on the EDT before showing the window
        EventQueue.invokeLater(() -> {
            AppContext.getInstance();          // initialises subdivision + demo users
            new MainJFrame().setVisible(true);
        });
    }

    // NetBeans designer stub
    @SuppressWarnings("unchecked")
    private void initComponents() { /* unused — UI built programmatically */ }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
