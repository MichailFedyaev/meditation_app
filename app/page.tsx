'use client';

import { useState } from 'react';
import { Howl } from 'howler';
import { PlayIcon, PauseIcon } from '@heroicons/react/24/solid';

const meditations = [
  {
    id: 1,
    title: 'Утренняя медитация',
    duration: '10 мин',
    description: 'Начните свой день с позитивной энергией',
    audioUrl: '/meditations/morning-meditation.mp3',
  },
  {
    id: 2,
    title: 'Медитация для сна',
    duration: '15 мин',
    description: 'Расслабляющая практика перед сном',
    audioUrl: '/meditations/sleep-meditation.mp3',
  },
  {
    id: 3,
    title: 'Дыхательные практики',
    duration: '8 мин',
    description: 'Техники глубокого дыхания для снятия стресса',
    audioUrl: '/meditations/breathing-meditation.mp3',
  },
];

export default function Home() {
  const [currentMeditation, setCurrentMeditation] = useState<number | null>(null);
  const [sound, setSound] = useState<Howl | null>(null);
  const [isPlaying, setIsPlaying] = useState(false);

  const playMeditation = (id: number) => {
    if (sound) {
      sound.stop();
    }
    const meditation = meditations.find(m => m.id === id);
    if (meditation) {
      const newSound = new Howl({
        src: [meditation.audioUrl],
        html5: true,
        onend: () => {
          setIsPlaying(false);
          setCurrentMeditation(null);
        },
      });
      setSound(newSound);
      newSound.play();
      setIsPlaying(true);
      setCurrentMeditation(id);
    }
  };

  const togglePlayPause = () => {
    if (sound) {
      if (isPlaying) {
        sound.pause();
      } else {
        sound.play();
      }
      setIsPlaying(!isPlaying);
    }
  };

  return (
    <main className="min-h-screen bg-background p-8">
      <div className="max-w-4xl mx-auto">
        <h1 className="text-4xl font-bold text-gray-900 mb-8">Медитация и Осознанность</h1>
        
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
          {meditations.map((meditation) => (
            <div
              key={meditation.id}
              className={`p-6 rounded-xl shadow-lg transition-all duration-300 ${
                currentMeditation === meditation.id
                  ? 'bg-primary text-white scale-105'
                  : 'bg-surface hover:scale-105'
              }`}
            >
              <h3 className="text-xl font-semibold mb-2">{meditation.title}</h3>
              <p className="text-sm opacity-75 mb-4">{meditation.description}</p>
              <div className="flex items-center justify-between">
                <span className="text-sm font-medium">{meditation.duration}</span>
                <button
                  onClick={() => currentMeditation === meditation.id ? togglePlayPause() : playMeditation(meditation.id)}
                  className="p-3 rounded-full bg-secondary text-white hover:bg-opacity-90 transition-colors"
                >
                  {currentMeditation === meditation.id && isPlaying ? (
                    <PauseIcon className="h-6 w-6" />
                  ) : (
                    <PlayIcon className="h-6 w-6" />
                  )}
                </button>
              </div>
            </div>
          ))}
        </div>
      </div>
    </main>
  );
} 