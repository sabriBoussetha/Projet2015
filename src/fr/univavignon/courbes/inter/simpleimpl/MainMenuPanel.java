package fr.univavignon.courbes.inter.simpleimpl;

/*
 * Courbes
 * Copyright 2015-16 L3 Info UAPV 2015-16
 * 
 * This file is part of Courbes.
 * 
 * Courbes is free software: you can redistribute it and/or modify it under the terms 
 * of the GNU General Public License as published by the Free Software Foundation, 
 * either version 2 of the License, or (at your option) any later version.
 * 
 * Courbes is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR 
 * PURPOSE. See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with Courbes. If not, see <http://www.gnu.org/licenses/>.
 */

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import fr.univavignon.courbes.inter.simpleimpl.MainWindow.PanelName;
import fr.univavignon.courbes.inter.simpleimpl.profiles.ProfileManager;
import fr.univavignon.courbes.sounds.simpleimp.SoundEffect;

/**
 * Panel contenant le menu principal du jeu.
 * 
 * @author	L3 Info UAPV 2015-16
 */
public class MainMenuPanel extends JPanel implements ActionListener
{	/** Numéro de série de la classe */
	private static final long serialVersionUID = 1L;
	/** Instance de la classe SoundEffect nécessaire pour lancer le son en arrière plan*/
	private SoundEffect sound;
	/** Boolean indiquant si le son en arrière plan est entrain de jouer ou pas */
	private boolean soundIsWorking = false;
	/** Index indiquant du morceau en cours */
	private static int indexOfSound = 0;
	/**
	 * Crée le menu principal et tous ses composants graphiques.
	 * 
	 * @param mainWindow
	 * 		Fenêtre principale contenant ce panel. 
	 */
	public MainMenuPanel(MainWindow mainWindow)
	{	super();
		
		this.mainWindow = mainWindow;
		sound = new SoundEffect();
		soundIsWorking = sound.backGroundMusic(true,indexOfSound);
		initMenu();
	}
	
	/** Fenêtre principale */
	private MainWindow mainWindow;
	
	/** Bouton pour démarrer une partie locale */
	private JButton localGameButton;
	/** Bouton pour démarrer une partie réseau */
	private JButton serverGameButton;
	/** Bouton pour rejoindre une partie réseau */
	private JButton clientGameButton;
	/** Bouton pour accéder à la liste des profils */
	private JButton profilesButton;
	/** Bouton pour accéder aux statistiques */
	private JButton statsButton;
	/** Bouton pour quitter le jeu */
	private JButton quitButton;
	/** Bouton pour arrêter/reprendre le son en arrière plan */
	private JButton offSound;
	/** Bouton pour passer au morceau suivant */
	private JButton nextSoundTrack;
	
	
	/**
	 * Initialisation du menu principal.
	 */
	public void initMenu()
	{	BorderLayout borderLayout = new BorderLayout();
		setLayout(borderLayout);

		JPanel menuPanel = new JPanel();
		BoxLayout boxLayout = new BoxLayout(menuPanel, BoxLayout.PAGE_AXIS);
		menuPanel.setLayout(boxLayout);
		
		menuPanel.add(Box.createVerticalGlue());
		
		// créer une partie locale
		localGameButton = initButton("Créer une partie locale",400,50);
		//localGameButton.setBackground(Color.orange); 
		menuPanel.add(localGameButton);
		
		// créer une partie réseau
		serverGameButton = initButton("Créer une partie réseau",400,50);
		//serverGameButton.setBackground(Color.orange); 
		menuPanel.add(serverGameButton);
		
		// rejoindre une partie réseau
		clientGameButton = initButton("Rejoindre une partie réseau",400,50);
		//clientGameButton.setBackground(Color.orange); 
		menuPanel.add(clientGameButton);
		
		menuPanel.add(Box.createVerticalStrut(10));
		
		// lister les profils existants
		profilesButton = initButton("Voir les profils",400,50);
		//profilesButton.setBackground(Color.green); 
		menuPanel.add(profilesButton);
		
		// afficher les statistiques
		statsButton = initButton("Voir les statistiques",400,50);
		//statsButton.setBackground(Color.green); 
		menuPanel.add(statsButton);
		
		menuPanel.add(Box.createVerticalStrut(10));
		
		// quitter le jeu
		quitButton = initButton("Quitter le jeu",400,50);
		//quitButton.setBackground(Color.red); 
		menuPanel.add(quitButton);
		
		menuPanel.add(Box.createVerticalGlue());
		add(menuPanel,BorderLayout.CENTER);
		menuPanel.setBackground(Color.BLACK);
		setVisible(true);
		
		// Panel qui contient le bouton pour arrêter/reprendre le son
		JPanel soundPanel = new JPanel();
		BoxLayout boxLayout1 = new BoxLayout(soundPanel, BoxLayout.LINE_AXIS);
		soundPanel.setLayout(boxLayout1);
		
		soundPanel.add(Box.createVerticalGlue());
		
		offSound = initButton("Mute",100,50);	
		soundPanel.add(offSound);
		
		nextSoundTrack = initButton("next",100,50);	
		soundPanel.add(nextSoundTrack);
		
		soundPanel.add(Box.createVerticalGlue());
		soundPanel.setBackground(Color.BLACK);
		add(soundPanel, BorderLayout.SOUTH);
		
		setVisible(true);
		
	}
	
	/**
	 * Initialise chaque bouton de la même façon.
	 * 
	 * @param text
	 * 		Texte à inclure dans le bouton.
	 * @param width
	 * 		Correspond à la largeur du bouton
	 * @param height
	 * 		La longueur du bouton
	 * @return
	 * 		Bouton convenablement configuré. 
	 */
	private JButton initButton(String text, int width, int height)
	{	JButton result = new JButton(text);
	
		Font font = getFont();
		font = new Font(font.getName(),Font.PLAIN,25);
		result.setFont(font);
		
		Dimension dim = new Dimension(width,height);
		result.setMaximumSize(dim);
		result.setMinimumSize(dim);
		result.setPreferredSize(dim);
		result.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		result.addActionListener(this);
		
		return result;
	}
	
	@Override
	public void actionPerformed(ActionEvent e)
	{	if(e.getSource()==localGameButton)	
		{	sound.clickSound(); // Son correspondant à un click
			if(ProfileManager.getProfiles().size() > 1)
			{	mainWindow.displayPanel(PanelName.LOCAL_GAME_PLAYER_SELECTION);
			}
			else
			{	sound.errorSound();
				JOptionPane.showMessageDialog
				(	mainWindow,
					"<html>Pour démarrer une partie locale, vous devez avoir défini au moins 2 profils."
					+ "<br/>(pour définir des profils, cliquez sur \"Profils\")</html>"
				);
			}
		}
	
		else if(e.getSource()==serverGameButton)
		{	sound.clickSound();
			mainWindow.displayPanel(PanelName.SERVER_GAME_PORT_SELECTION);
		}
	
		else if(e.getSource()==clientGameButton)
		{	sound.clickSound(); // Son correspondant à un click
			if (ProfileManager.getProfiles().size() > 0)
			{	mainWindow.displayPanel(PanelName.CLIENT_GAME_PLAYER_SELECTION);
			}
			else
			{	sound.errorSound();
				JOptionPane.showMessageDialog
				(	mainWindow,
					"<html>Pour démarrer un client, vous devez avoir défini au moins 1 profil."
					+ "<br/>(pour définir des profils, cliquez sur \"Profils\")</html>"
				);
			}
		}
	
		else if(e.getSource()==profilesButton)
		{	sound.clickSound(); // Son correspondant à un click
			mainWindow.displayPanel(PanelName.PROFILE_LIST);
		}
	
		else if(e.getSource()==statsButton)
		{	sound.clickSound(); // Son correspondant à un click
			mainWindow.displayPanel(PanelName.STATISTICS);
		}
	
		else if(e.getSource()==quitButton)
		{	sound.clickSound(); // Son correspondant à un click
			mainWindow.closeWindow();
		}
		else if(e.getSource()==offSound)
		{	
			if(soundIsWorking)
			{	soundIsWorking = sound.backGroundMusic(false,indexOfSound);
				offSound.setText("Son");	// Changer le texte du bouton
			}
			else// if(!soundIsWorking)
			{	soundIsWorking = sound.backGroundMusic(true,indexOfSound);
				offSound.setText("Mute");
			}
		}
		else if(e.getSource()==nextSoundTrack)
		{	
			if(indexOfSound==sound.soundTrack.length-1)
			{	soundIsWorking = sound.backGroundMusic(false,indexOfSound);
				indexOfSound = 0;
				soundIsWorking = sound.backGroundMusic(true, 0);
				offSound.setText("Mute");	// Changer le texte du bouton
			}
			else
			{	soundIsWorking = sound.backGroundMusic(false,indexOfSound);
				indexOfSound += 1;
				soundIsWorking = sound.backGroundMusic(true, indexOfSound);
				offSound.setText("Mute");	// Changer le texte du bouton
			}
		}
	
	}

	
}
